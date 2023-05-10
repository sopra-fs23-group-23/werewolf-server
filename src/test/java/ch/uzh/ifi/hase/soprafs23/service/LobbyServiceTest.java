package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.RoleInformationComparator;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Villager;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Werewolf;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Witch;
import ch.uzh.ifi.hase.soprafs23.rest.dto.RoleGetDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
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
    void testValidateLobbyIsOpen() {
        Lobby lobby = mock(Lobby.class);
        when(lobby.isOpen()).thenReturn(true);
        lobbyService.validateLobbyIsOpen(lobby);
    }

    @Test
    void testValidateLobbyIsOpen_closedLobby() {
        Lobby lobby = mock(Lobby.class);
        when(lobby.isOpen()).thenReturn(false);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->lobbyService.validateLobbyIsOpen(lobby));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testJoinUserToLobby() {
        User admin = createTestAdmin();
        User joiningUser = new User();
        joiningUser.setId(2l);
        joiningUser.setUsername("Test");
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(admin));
        lobbyService.joinUserToLobby(joiningUser, lobby);
        assertTrue(lobby.getPlayers().stream().anyMatch(p->p.getId() == joiningUser.getId()), "Joining player was not added to lobby");
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
    void testValidateLobbySize() {
        Lobby lobby = mock(Lobby.class);
        Mockito.when(lobby.getLobbySize()).thenReturn(Lobby.MIN_SIZE);
        lobbyService.validateLobbySize(lobby);
        // should not throw error
    }

    @Test
    void testValidateLobbySize_tooSmall() {
        Lobby lobby = mock(Lobby.class);
        Mockito.when(lobby.getLobbySize()).thenReturn(Lobby.MIN_SIZE-1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->lobbyService.validateLobbySize(lobby));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testValidateLobbySize_tooLarge() {
        Lobby lobby = mock(Lobby.class);
        Mockito.when(lobby.getLobbySize()).thenReturn(Lobby.MAX_SIZE+1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->lobbyService.validateLobbySize(lobby));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testGetAllRolesInformation() {
        Collection<Role> rolesReturn= new ArrayList<>();
        Lobby mock = mock(Lobby.class);
        rolesReturn.add(new Werewolf(mock::getAlivePlayers));
        Mockito.when(mock.getRoles()).thenReturn(rolesReturn);
        ArrayList<RoleGetDTO> roleGetDTOS = new ArrayList<>(lobbyService.getAllRolesInformation(mock));
        assertEquals("Werewolf", roleGetDTOS.get(0).getRoleName());
        assertEquals(0, roleGetDTOS.get(0).getAmount());
    }

    @Test
    void testCloseLobby() {
        Lobby lobby = mock(Lobby.class);
        lobbyService.closeLobby(lobby);
        verify(lobby).setOpen(false);
    }

    @Test
    void testGetPlayerRoleInformation() {
        Collection<Role> rolesReturn = new ArrayList<>();
        Lobby lobby = mock(Lobby.class);
        Player player = mock(Player.class);
        rolesReturn.add(new Werewolf(lobby::getAlivePlayers));

        Mockito.when(lobby.getRolesOfPlayer(player)).thenReturn(rolesReturn);
        
        ArrayList<RoleGetDTO> roleGetDTOS = new ArrayList<>(lobbyService.getPlayerRoleInformation(player, lobby, new RoleInformationComparator()));
        assertEquals("Werewolf", roleGetDTOS.get(0).getRoleName());
        assertEquals(0, roleGetDTOS.get(0).getAmount());
    }

    @Test
    void testGetPlayerRoleInformation_Witch() {
        Collection<Role> rolesReturn = new ArrayList<>();
        Lobby lobby = mock(Lobby.class);
        Player player = mock(Player.class);
        rolesReturn.add(new Villager(null, null, null));
        rolesReturn.add(new Witch(null, null, null));

        Mockito.when(lobby.getRolesOfPlayer(player)).thenReturn(rolesReturn);
        
        ArrayList<RoleGetDTO> roleGetDTOS = new ArrayList<>(lobbyService.getPlayerRoleInformation(player, lobby, new RoleInformationComparator()));
        assertEquals("Witch", roleGetDTOS.get(0).getRoleName());
        assertEquals(0, roleGetDTOS.get(0).getAmount());
    }

    @Test
    void testGetPlayerOfUser() {
        User user = mock(User.class);
        Lobby lobby = mock(Lobby.class);
        Player player = mock(Player.class);
        Mockito.when(user.getId()).thenReturn(1l);
        Mockito.when(lobby.getPlayerById(1l)).thenReturn(player);
        
        assertEquals(player, lobbyService.getPlayerOfUser(user, lobby));
    }
}
