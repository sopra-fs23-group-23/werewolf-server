package ch.uzh.ifi.hase.soprafs23.controller;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ch.uzh.ifi.hase.soprafs23.service.UserService.USERAUTH_HEADER;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.rest.dto.RoleGetDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.rest.logicmapper.LogicEntityMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;

import java.util.ArrayList;
import java.util.Collection;

@WebMvcTest(LobbyController.class)
public class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private UserService userService;

    private User createTestUser(String username, Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        return user;
    }

    @Test
    void createNewLobbyTest() throws Exception {
        User tUser = createTestUser("test", 1L);
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(tUser));
        Mockito.when(userService.getUserByToken("token")).thenReturn(tUser);
        Mockito.when(lobbyService.createNewLobby(tUser)).thenReturn(lobby);

        MockHttpServletRequestBuilder postRequest = post("/lobbies")
            .header(USERAUTH_HEADER, "token");

        mockMvc.perform(postRequest)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
            .andExpect(jsonPath("$.admin.id", is(tUser.getId().intValue())));
    }

    @Test
    void joinLobbyTest() throws Exception {
        User admin = createTestUser("admin", 1l);
        User joiningUser = createTestUser("joiningUser", 2l);
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(admin));

        Mockito.when(userService.getUserByToken("token")).thenReturn(joiningUser);
        Mockito.when(lobbyService.getLobbyById(1l)).thenReturn(lobby);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/1")
            .header(USERAUTH_HEADER, "token");

        mockMvc.perform(putRequest)
            .andExpect(status().isNoContent());

        verify(lobbyService).joinUserToLobby(joiningUser, lobby);
    }

    @Test
    void testGetLobbyInformation() throws Exception {
        User admin = createTestUser("admin", 1l);
        User usr = createTestUser("user", 2l);
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(admin));
        lobby.addPlayer(LogicEntityMapper.createPlayerFromUser(usr));

        Mockito.when(userService.getUserByToken("token")).thenReturn(usr);
        Mockito.when(lobbyService.getLobbyById(1l)).thenReturn(lobby);
        doNothing().when(lobbyService).validateUserIsInLobby(usr, lobby);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1")
            .header(USERAUTH_HEADER, "token");

        mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
            .andExpect(jsonPath("$.admin.id", is(admin.getId().intValue())))
            .andExpect(jsonPath(String.format("$.players[?(@.id == %d)]", usr.getId())).exists());
    }

    @Test
    void testGetAllRoles() throws Exception {
        User user = createTestUser("test", 1L);
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(user));
        Collection<RoleGetDTO> mockReturn = new ArrayList<>();

        Mockito.when(userService.getUserByToken(Mockito.anyString())).thenReturn(user);
        Mockito.when(lobbyService.getLobbyById(1L)).thenReturn(lobby);
        doNothing().when(lobbyService).validateUserIsInLobby(user, lobby);
        Mockito.when(lobbyService.getAllRolesInformation(lobby)).thenReturn(mockReturn);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/roles").
                header(USERAUTH_HEADER, "token");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    void testGetOwnRoles() throws Exception {
        User user = createTestUser("test", 1L);
        Player player = LogicEntityMapper.createPlayerFromUser(user);
        Lobby lobby = new Lobby(1L, player);

        Mockito.when(lobbyService.getLobbyById(1L)).thenReturn(lobby);
        Mockito.when(userService.getUser(1L)).thenReturn(user);
        doNothing().when(userService).validateTokenMatch(user, "token");
        doNothing().when(lobbyService).validateUserIsInLobby(user, lobby);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/roles/1").
                header(USERAUTH_HEADER, "token");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }
}
