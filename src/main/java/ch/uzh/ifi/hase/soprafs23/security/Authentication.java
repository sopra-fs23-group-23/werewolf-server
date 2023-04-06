package ch.uzh.ifi.hase.soprafs23.security;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import java.util.stream.StreamSupport;

public class Authentication {

    private final UserRepository userRepository;
    private final LobbyService lobbyService;

    public Authentication(@Qualifier("userRepository") UserRepository userRepository, LobbyService aLobbyService) {
        this.userRepository = userRepository;
        this.lobbyService = aLobbyService;
    }

    public void authenticateUser(String token){
        if (userRepository.findByToken(token) == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this token does not exist.");
        }
    }

    // TODO: encapsulate validateUserInLobby to not let changes in lobbyservice etc. influence the behaviour of the auth process.
    // On which layer should lobbies be accessed?
    // How to access lobby and Users?
    public void validateUserIsInLobby(String token, String lobbyId) {
        authenticateUser(token);
        User authUser = userRepository.findByToken(token);
        Lobby aLobby = this.lobbyService.getLobbyById(Long.parseLong(lobbyId)); // Throws exception if Lobby doesn't exist | dependency on lobbyService maybe critical

        /* Uncomment if Lobby existence needs to be checked.
        if (LobbyService.getLobbyById(lobbyId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Lobby with id %d does not exist", lobby));
        }
        */

        if (!StreamSupport.stream(aLobby.getPlayers().spliterator(), false).anyMatch(p->p.getId()==authUser.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not part of this lobby");
        }

    }

}
