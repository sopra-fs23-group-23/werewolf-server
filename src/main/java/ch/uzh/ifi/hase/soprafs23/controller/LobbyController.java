package ch.uzh.ifi.hase.soprafs23.controller;

import static ch.uzh.ifi.hase.soprafs23.service.LobbyService.LOBBYID_PATHVARIABLE;
import static ch.uzh.ifi.hase.soprafs23.service.UserService.USERAUTH_HEADER;

import java.util.Collection;
import java.util.Objects;

import ch.uzh.ifi.hase.soprafs23.rest.dto.RoleGetDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.RoleInformationComparator;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbySettingsDTO;
import ch.uzh.ifi.hase.soprafs23.rest.logicmapper.LogicDTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.web.server.ResponseStatusException;

/**
 * This class handles all requests related to lobby
 */

@RestController
public class LobbyController {
    private final UserService userService;
    private final LobbyService lobbyService;

    public LobbyController(UserService userService, LobbyService lobbyService) {
        this.userService = userService;
        this.lobbyService = lobbyService;
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createNewLobby(@RequestHeader(USERAUTH_HEADER) String userToken) {
        User user = userService.getUserByToken(userToken);
        Lobby l = lobbyService.createNewLobby(user);
        return LogicDTOMapper.convertLobbyToLobbyGetDTO(l);
    }

    @PutMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void joinLobby(@PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId, @RequestHeader(USERAUTH_HEADER) String userToken) {
        User user = userService.getUserByToken(userToken);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateLobbyIsOpen(lobby);
        lobbyService.joinUserToLobby(user, lobby);
    }

    @DeleteMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void leaveLobby(@PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId, @RequestHeader(USERAUTH_HEADER) String userToken) {
        User user = userService.getUserByToken(userToken);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateLobbyIsOpen(lobby);
        lobbyService.validateUserIsInLobby(user, lobby);
        if (lobbyService.userIsAdmin(user, lobby)) {
            lobbyService.dissolveLobby(lobby);
        } else {
            lobbyService.removeUserFromLobby(user, lobby);
        }
    }

    @GetMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobbyInformation(@PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId, @RequestHeader(USERAUTH_HEADER) String userToken) {
        User user = userService.getUserByToken(userToken);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateUserIsInLobby(user, lobby);
        return LogicDTOMapper.convertLobbyToLobbyGetDTO(lobby);
    }

    @GetMapping("/lobbies/{lobbyId}/roles")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<RoleGetDTO> getAllRoles(@PathVariable(LOBBYID_PATHVARIABLE) Long LobbyId, @RequestHeader(USERAUTH_HEADER) String token) {
        User user = userService.getUserByToken(token);
        Lobby lobby = lobbyService.getLobbyById(LobbyId);
        lobbyService.validateUserIsInLobby(user, lobby);
        return lobbyService.getAllRolesInformation(lobby);
    }

    @GetMapping("/lobbies/{lobbyId}/roles/{uid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<RoleGetDTO> getPlayerRole(@PathVariable(LOBBYID_PATHVARIABLE) Long LobbyId, @PathVariable("uid") Long userId,
                                             @RequestHeader(USERAUTH_HEADER) String token) {
        User user = userService.getUserByToken(token);
        User userToGetRole = userService.getUser(userId);
        Lobby lobby = lobbyService.getLobbyById(LobbyId);
        lobbyService.validateUserIsInLobby(user, lobby);
        lobbyService.validateUserIsInLobby(userToGetRole, lobby);
        Player playerToGetRole = lobbyService.getPlayerOfUser(userToGetRole, lobby);
        if (playerToGetRole.isAlive() || playerToGetRole.isRevivable()) {
            // if player is alive, only the user himself can see his role
            userService.validateTokenMatch(userToGetRole, token);
        }
        return lobbyService.getPlayerRoleInformation(playerToGetRole, lobby, new RoleInformationComparator());
    }

    @GetMapping("/users/{uid}/lobby")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobbyOfUser(@PathVariable("uid") Long userId, @RequestHeader(USERAUTH_HEADER) String token) {
        User user = userService.getUserByToken(token);
        userService.validateTokenMatch(user, token);
        Lobby lobby = lobbyService.getLobbyOfUser(userId);
        if(lobby == null) {
            return null;
        }
        return LogicDTOMapper.convertLobbyToLobbyGetDTO(lobby);
    }

    @PutMapping("/lobbies/{lobbyId}/settings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateLobbySettings(@PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId, @RequestHeader(USERAUTH_HEADER) String token,
                                    @RequestBody LobbySettingsDTO settingsDTO) {
        User user = userService.getUserByToken(token);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateUserIsAdmin(user, lobby);
        lobbyService.updateLobbySettings(lobby, settingsDTO);
    }

    @GetMapping("/lobbies/{lobbyId}/settings")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbySettingsDTO getLobbySettings(@PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId, @RequestHeader(USERAUTH_HEADER) String token) {
        User user = userService.getUserByToken(token);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateUserIsInLobby(user, lobby);
        return LogicDTOMapper.convertLobbyToLobbySettingsDTO(lobby);
    }
}
