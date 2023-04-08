package ch.uzh.ifi.hase.soprafs23.security;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import java.util.stream.StreamSupport;

public class Authentication {

    private final UserRepository userRepository;

    private final LobbyService lobbyService;

    public Authentication(UserRepository userRepository, LobbyService lobbyService) {
        this.userRepository = userRepository;
        this.lobbyService = lobbyService;
    }

    public void authenticateUser(String token){
        if (this.userRepository.findByToken(token) == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User with this token does not exist. 1");
        }
    }

    public void validateUserInLobby(String token, String lobbyId) {
        authenticateUser(token);
        User authUser = this.userRepository.findByToken(token);
        Lobby aLobby = this.lobbyService.getLobbyById(Long.parseLong(lobbyId)); // Throws exception if Lobby doesn't exist

        if (!StreamSupport.stream(aLobby.getPlayers().spliterator(), false).anyMatch(p->p.getId()==authUser.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not part of this lobby");
        }

    }

}
