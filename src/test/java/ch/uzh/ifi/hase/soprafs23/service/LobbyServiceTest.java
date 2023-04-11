package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.StreamSupport;

import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Werewolf;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import ch.uzh.ifi.hase.soprafs23.constant.sse.LobbySseEvent;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.rest.logicmapper.LogicEntityMapper;

public class LobbyServiceTest {
    LobbyService lobbyService = new LobbyService();

    private User createTestAdmin() {
        return createTestUser(1l, "admin");
    }

    private User createTestUser(Long id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        return user;
    }

    private void joinN_Users(int n, Lobby lobby) {
        for (long i = 2L; i < n+2; i++) {
            lobbyService.joinUserToLobby(createTestUser(i, "user" + Long.toString(i)), lobby);
        }
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
        lobbyService.createNewLobby(admin);
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
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(admin));
        lobbyService.joinUserToLobby(joiningUser, lobby);
        assertTrue(StreamSupport.stream(lobby.getPlayers().spliterator(), false).anyMatch(p->p.getId() == joiningUser.getId()), "Joining player was not added to lobby");
    }

    @Test
    void testJoinUserToLobby_userInAnotherLobby() {
        User admin1 = createTestAdmin();
        User admin2 = createTestAdmin();
        admin2.setId(2l);
        User joiningUser = createTestUser(3l, "test");

        Lobby lobby1 = lobbyService.createNewLobby(admin1);
        Lobby lobby2 = lobbyService.createNewLobby(admin2);
        lobbyService.joinUserToLobby(joiningUser, lobby1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->lobbyService.joinUserToLobby(joiningUser, lobby2));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testJoinUserToLobby_userInThisLobby() {
        User admin = createTestAdmin();
        User joiningUser = createTestUser(2l, "test");

        Lobby lobby = lobbyService.createNewLobby(admin);
        lobbyService.joinUserToLobby(joiningUser, lobby);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->lobbyService.joinUserToLobby(joiningUser, lobby));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testJoinUserToLobby_lobbyFull() {
        User joiningUser = createTestUser(2l, "test");

        Lobby mock = mock(Lobby.class);
        Mockito.when(mock.getLobbySize()).thenReturn(Lobby.MAX_SIZE);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->lobbyService.joinUserToLobby(joiningUser, mock));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testJoinUserToLobby_lobbyClosed() {
        User joiningUser = createTestUser(2l, "test");

        Lobby mock = mock(Lobby.class);
        Mockito.when(mock.isOpen()).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->lobbyService.joinUserToLobby(joiningUser, mock));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testValidateUserIsInLobby() {
        User admin = createTestAdmin();
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(admin));
        lobbyService.validateUserIsInLobby(admin, lobby);
    }

    @Test
    void testValidateUserIsInLobby_userNotInLobby() {
        User admin = createTestAdmin();
        User randoUser = new User();
        randoUser.setId(2l);
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(admin));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->lobbyService.validateUserIsInLobby(randoUser, lobby));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void testCreateAndGetLobbyEmitter() {
        User admin = createTestAdmin();
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(admin));
        SseEmitter emitter = lobbyService.createLobbyEmitter(lobby);
        assertEquals(emitter, lobbyService.getLobbyEmitter(lobby));


    }

    @Test
    void testSendEmitterUpdate() throws IOException {
        SseEmitter mockEmitter = mock(SseEmitter.class);
        lobbyService.sendEmitterUpdate(mockEmitter, "test", LobbySseEvent.update);
        Mockito.verify(mockEmitter).send(Mockito.any(SseEventBuilder.class));
    }

    @Test
    void testGetPlayerByUser_Valid() {
        User admin = createTestAdmin();
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(admin));
        User joiningUser = new User();
        joiningUser.setId(3L);
        joiningUser.setUsername("TestUser");
        lobbyService.joinUserToLobby(joiningUser, lobby);
        assertEquals(lobbyService.getPlayerByUser(joiningUser, lobby).getId(), joiningUser.getId());
    }

    @Test
    void testGetPlayerByUser_NotInLobby() {
        User admin = createTestAdmin();
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(admin));
        User notJoiningUser = new User();
        notJoiningUser.setId(3L);
        notJoiningUser.setUsername("TestUser");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->lobbyService.getPlayerByUser(notJoiningUser, lobby));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void assignRole_notAdmin() {
        User admin = createTestAdmin();
        User notAdmin = createTestUser(15L, "notAdmin");
        Lobby lobby = lobbyService.createNewLobby(admin);
        lobbyService.joinUserToLobby(notAdmin, lobby);
        joinN_Users(5, lobby);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->lobbyService.assignRoles(notAdmin, lobby));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void assignRole_allowed() {
        User admin = createTestAdmin();
        Lobby lobby = lobbyService.createNewLobby(admin);
        joinN_Users(5, lobby);
        lobbyService.assignRoles(admin, lobby);
        ArrayList<Role> roles = new ArrayList<>(lobby.getRoles());
        boolean foundWerewolf = false;
        for (Role role: roles) {
            if (role.getClass() == Werewolf.class) {
                foundWerewolf = true;
                assertEquals("Werewolf", role.getName());
                break;
            }
        }
        assertTrue(foundWerewolf);
    }
}
