package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Werewolf;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.rest.logicmapper.LogicEntityMapper;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RoleServiceTest {

    LobbyService lobbyService = new LobbyService();
    RoleService roleService = new RoleService();

    private User createUser(long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("gollum");
        return user;
    }

    private void joinN_Users(int n, Lobby lobby) {
        for (long i = 2; i < n+2; i++) {
            lobbyService.joinUserToLobby(createUser(i), lobby);
        }
    }

    @Test
    void assignRole_notAdmin() {
        User admin = createUser(1L);
        User notAdmin = createUser(15L);
        Lobby lobby = lobbyService.createNewLobby(admin);
        lobbyService.joinUserToLobby(notAdmin, lobby);
        joinN_Users(5, lobby);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->roleService.assignRoles(notAdmin, lobby));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void assignRole_allowed() {
        User admin = createUser(1L);
        Lobby lobby = lobbyService.createNewLobby(admin);
        joinN_Users(5, lobby);
        roleService.assignRoles(admin, lobby);
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
