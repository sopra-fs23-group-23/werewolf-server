package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.service.AgoraService;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.mock;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static ch.uzh.ifi.hase.soprafs23.service.UserService.USERAUTH_HEADER;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(AgoraController.class)
public class AgoraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private UserService userService;

    @MockBean
    private AgoraService agoraService;

    private User user = mock(User.class);
    private Lobby lobby = mock(Lobby.class);

    @Test
    public void testGetVoiceChannelToken() throws Exception {
        Mockito.when(userService.getUserByToken("token")).thenReturn(user);
        Mockito.when(lobbyService.getLobbyById(1l)).thenReturn(lobby);

        MockHttpServletRequestBuilder getRequest = get("/agora/1/token")
                .header(USERAUTH_HEADER, "token");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());

        verify(agoraService).createVoiceChannelToken(lobby, user);
    }
}
