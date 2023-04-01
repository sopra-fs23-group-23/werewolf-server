package ch.uzh.ifi.hase.soprafs23.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;

@WebMvcTest(LobbyController.class)
public class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private UserService userService;

    @Test
    void createNewLobbyTest() throws Exception {
        User tUser = new User();
        tUser.setId(1l);
        Lobby lobby = new Lobby(1L, new Player(1L, "test"));
        Mockito.when(userService.getUser(1l)).thenReturn(tUser);
        Mockito.when(lobbyService.createNewLobby(tUser)).thenReturn(lobby);

        MockHttpServletRequestBuilder postRequest = post("/lobbies")
            .header("uid", 1);

        mockMvc.perform(postRequest)
            .andExpect(content().string(Long.toString(lobby.getId())))
            .andExpect(status().isCreated());
    }

    @Test
    void joinLobbyTest() throws Exception {
        User admin = new User();
        User joiningUser = new User();
        joiningUser.setId(2l);
        admin.setId(1l);
        admin.setUsername("admin");
        Lobby lobby = new Lobby(1L, new Player(admin.getId(), admin.getUsername()));

        Mockito.when(userService.getUser(Mockito.anyLong())).thenReturn(joiningUser);
        Mockito.when(lobbyService.getLobbyById(1l)).thenReturn(lobby);
        doNothing().when(lobbyService).joinUserToLobby(joiningUser, lobby);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/1")
            .header("uid", 2);

        mockMvc.perform(putRequest)
            .andExpect(status().isNoContent());
    }
}
