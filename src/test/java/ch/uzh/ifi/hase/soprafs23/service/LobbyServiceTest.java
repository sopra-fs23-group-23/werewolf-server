package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public class LobbyServiceTest {
    LobbyService lobbyService = new LobbyService();

    private User createTestAdmin() {
        User admin = new User();
        admin.setId(1l);
        admin.setUsername("admin");
        return admin;
    }

    @Test
    void testCreateNewLobby() {
        User admin = createTestAdmin();
        Lobby lobby = lobbyService.createNewLobby(admin);
        assertEquals(admin.getId(), lobby.getAdmin().getId());
        assertTrue(100000 <= lobby.getId() && lobby.getId() <= 999999, "Lobby ID is out of range");
        assertTrue(lobbyService.getLobbies().contains(lobby), "Lobby not stored after creation");
    }

    @Test
    void testCreateNewLobby_adminAlreadyHasLobby() {
        User admin = createTestAdmin();
        Lobby lobby = lobbyService.createNewLobby(admin);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> lobbyService.createNewLobby(admin));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testGetLobbyById() {
        User admin = createTestAdmin();
        Lobby expected = lobbyService.createNewLobby(admin);
        Lobby actual = lobbyService.getLobbyById(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    void testGetLobbyById_nonExistent() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> lobbyService.getLobbyById(1l));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testJoinUserToLobby() {
        User admin = createTestAdmin();
        User joiningUser = new User();
        joiningUser.setId(2l);
        joiningUser.setUsername("Test");
        Lobby lobby = new Lobby(1L, new Player(admin.getId(), admin.getUsername()));
        lobbyService.joinUserToLobby(joiningUser, lobby);
        assertTrue(StreamSupport.stream(lobby.getPlayers().spliterator(), false).anyMatch(p->p.getId() == joiningUser.getId()), "Joining player was not added to lobby");
    }

    @Test
    void testJoinUserToLobby_userInAnotherLobby() {
        User admin1 = createTestAdmin();
        User admin2 = createTestAdmin();
        admin2.setId(2l);
        User joiningUser = new User();
        joiningUser.setId(3l);
        joiningUser.setUsername("Test");

        Lobby lobby1 = lobbyService.createNewLobby(admin1);
        Lobby lobby2 = lobbyService.createNewLobby(admin2);
        lobbyService.joinUserToLobby(joiningUser, lobby1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->lobbyService.joinUserToLobby(joiningUser, lobby2));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}