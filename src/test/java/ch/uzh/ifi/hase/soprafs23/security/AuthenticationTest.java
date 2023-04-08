package ch.uzh.ifi.hase.soprafs23.security;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.logicmapper.LogicEntityMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class AuthenticationTest {

    UserRepository userRepository = Mockito.mock(UserRepository.class);
    LobbyService lobbyService = Mockito.mock(LobbyService.class);
    Authentication authentication = new Authentication(userRepository, lobbyService);

    @Test
    void authenticateExistingUser() {
        User aUser = new User();
        String token = "valid-token";

        Mockito.when(userRepository.findByToken(token)).thenReturn(aUser);
        authentication.authenticateUser(token);
    }

    @Test
    void authenticateNonExistingUser() {
        String token = "invalid-token";

        given(userRepository.findByToken(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "User with this token does not exist."));

        // Use a lambda expression to assert that the method throws an exception
        assertThrows(ResponseStatusException.class, () -> {
            authentication.authenticateUser(token);
        });

        // You can also assert the details of the exception message, if you want
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authentication.authenticateUser(token);
        });
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void validateUserInLobby_UserIsInLobby(){
        User aUser = new User();
        Player aPlayer = new Player(aUser.getId(), "Willy Player");
        Lobby aLobby = new Lobby((long) 12345, aPlayer);
        Mockito.when(userRepository.findByToken(Mockito.any())).thenReturn(aUser);
        Mockito.when(lobbyService.getLobbyById(Mockito.any())).thenReturn(aLobby); // Fails because of false implementation of Lobby?
        Mockito.when(aLobby.getPlayers()).thenReturn((Iterable<Player>) aPlayer);
        authentication.validateUserInLobby("token", "lobbyId");
    }

    @Test
    void validateUserInLobby_UserNotInLobby(){
        User userA = new User();
        User userB = new User();
        Player aPlayer = new Player(userB.getId(), "Willy Player");
        Lobby aLobby = new Lobby((long) 12345, aPlayer);
        Mockito.when(userRepository.findByToken(Mockito.any())).thenReturn(userA);
        Mockito.when(lobbyService.getLobbyById(Mockito.any())).thenReturn(aLobby); // Fails because of false implementation of Lobby?
        Mockito.when(aLobby.getPlayers()).thenReturn((Iterable<Player>) aPlayer);
        authentication.validateUserInLobby("token", "lobbyId");
        // Use a lambda expression to assert that the method throws an exception
        assertThrows(ResponseStatusException.class, () -> {
            authentication.validateUserInLobby("invalid-token", "invalid-lobbyId");
        });

        // You can also assert the details of the exception message, if you want
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authentication.validateUserInLobby("invalid-token", "invalid-lobbyId");
        });
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

}
