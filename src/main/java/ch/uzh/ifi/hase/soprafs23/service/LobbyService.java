package ch.uzh.ifi.hase.soprafs23.service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import javax.transaction.Transactional;

import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.rest.dto.RoleGetDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.LobbyObserver;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.rest.logicmapper.LogicDTOMapper;
import ch.uzh.ifi.hase.soprafs23.rest.logicmapper.LogicEntityMapper;

@Service
@Transactional
public class LobbyService implements LobbyObserver{
    public static final String LOBBYID_PATHVARIABLE = "lobbyId";

    private Map<Long, Lobby> lobbies = new HashMap<>();

    private Long createLobbyId() {
        Long newId = ThreadLocalRandom.current().nextLong(100000, 999999);
        if (lobbies.containsKey(newId)) {
            return createLobbyId();
        }
        return newId;
    }

    public Lobby createNewLobby(User creator) {
        Player admin = LogicEntityMapper.createPlayerFromUser(creator);
        if(lobbies.values().stream().anyMatch(l -> l.getAdmin().equals(admin))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already has a lobby");
        }
        Lobby l = new Lobby(createLobbyId(), admin);
        l.addObserver(this);
        lobbies.put(l.getId(), l);
        return l;
    }

    public Collection<Lobby> getLobbies() {
        return lobbies.values();
    }

    public Lobby getLobbyById(Long lobbyId) {
        if (!lobbies.containsKey(lobbyId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Lobby with id %d does not exist", lobbyId));
        }
        return lobbies.get(lobbyId);
    }

    private boolean userInALobby(User user) {
        return lobbies.values().stream().anyMatch(
            l -> l.getPlayers().stream().anyMatch(p->p.getId().equals(user.getId()))
        );
    }

    private boolean userIsInLobby(User user, Lobby lobby) {
        return lobby.getPlayers().stream().anyMatch(p->p.getId().equals(user.getId()));
    }

    public void validateUserIsInLobby(User user, Lobby lobby) {
        if (!userIsInLobby(user, lobby)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not part of this lobby");
        }
    }

    /**
     * @pre user is in lobby
     * @param user
     * @param lobby
     * @return
     */
    public Player getPlayerOfUser(User user, Lobby lobby) {
        return lobby.getPlayerById(user.getId());
    }

    public void validateLobbyIsOpen(Lobby lobby) {
        if (!lobby.isOpen()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby is closed.");
        }
    }

    public void joinUserToLobby(User user, Lobby lobby) {
        if (lobby.getLobbySize() >= Lobby.MAX_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby is already full.");
        }
        if (!lobby.isOpen()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby is closed.");
        }
        if (userIsInLobby(user, lobby)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already in this lobby.");
        }
        if (userInALobby(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already in a lobby.");
        }
        lobby.addPlayer(LogicEntityMapper.createPlayerFromUser(user));
    }

    public void validateUserIsAdmin(User user, Lobby lobby) {
        if (!user.getId().equals(lobby.getAdmin().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the admin may perform this action.");
        }
    }

    public void validateLobbySize(Lobby lobby) {
        if (lobby.getLobbySize() > Lobby.MAX_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby has too many players.");
        }
        if (lobby.getLobbySize() < Lobby.MIN_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby has not enough players.");
        }
    }

    public Collection<RoleGetDTO> getAllRolesInformation(Lobby lobby) {
        return lobby.getRoles().stream().map(role -> LogicDTOMapper.convertRoleToRoleGetDTO(role)).toList();
    }

    /**
     * @pre player is in lobby
     * @param player
     * @param lobby
     * @return
     */
    public Collection<RoleGetDTO> getPlayerRoleInformation(Player player, Lobby lobby, Comparator<Role> comparator) {
        return lobby.getRolesOfPlayer(player).stream()
            .sorted(comparator)
            .map(role -> LogicDTOMapper.convertRoleToRoleGetDTO(role))
            .toList();
    }

    public void instantiateRoles(Lobby lobby, Game game) {
        lobby.instantiateRoles(lobby::getAlivePlayers, lobby::addPlayerToRole, game::getCurrentStagePollCommands, game::removePollCommandFromCurrentStage, game::addPollCommandToCurrentStage, lobby::getRolesOfPlayer);
    }

    /**
     * @pre executing user is admin, lobby roles instantiated
     * @param lobby
     */

    public void assignRoles(Lobby lobby) {
        lobby.assignRoles();
    }

    public void closeLobby(Lobby lobby) {
        lobby.setOpen(false);
    }

    /**
     * @pre lobbies contains lobby
     * @param lobby
     */
    public void removeLobby(Lobby lobby) {
        assert lobbies.containsKey(lobby.getId());
        lobbies.remove(lobby.getId());
    }

    @Override
    public void onLobbyDissolved(Lobby lobby) {
        if (lobbies.containsKey(lobby.getId())) {
            lobbies.remove(lobby.getId());
        }
    }
}
