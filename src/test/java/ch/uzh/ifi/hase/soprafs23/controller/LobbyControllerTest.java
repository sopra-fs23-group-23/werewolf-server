package ch.uzh.ifi.hase.soprafs23.controller;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import ch.uzh.ifi.hase.soprafs23.agora.RTCTokenBuilder;
import ch.uzh.ifi.hase.soprafs23.constant.VoiceChatRole;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.rest.logicmapper.LogicEntityMapper;
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

    private User createTestUser(String username, Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        return user;
    }

    @Test
    void createNewLobbyTest() throws Exception {
        User tUser = createTestUser("test", 1l);
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(tUser));
        Mockito.when(userService.getUser(1l)).thenReturn(tUser);
        Mockito.when(lobbyService.createNewLobby(tUser)).thenReturn(lobby);
        Mockito.when(lobbyService.createLobbyEmitter(lobby)).thenReturn(null);

        MockHttpServletRequestBuilder postRequest = post("/lobbies")
            .header("uid", 1);

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

        Mockito.when(userService.getUser(Mockito.anyLong())).thenReturn(joiningUser);
        Mockito.when(lobbyService.getLobbyById(1l)).thenReturn(lobby);
        doNothing().when(lobbyService).joinUserToLobby(joiningUser, lobby);
        SseEmitter mockSseEmitter = mock(SseEmitter.class);
        Mockito.when(lobbyService.getLobbyEmitter(lobby)).thenReturn(mockSseEmitter);
        doNothing().when(lobbyService).sendEmitterUpdate(Mockito.any(SseEmitter.class), Mockito.anyString());

        MockHttpServletRequestBuilder putRequest = put("/lobbies/1")
            .header("uid", 2);

        mockMvc.perform(putRequest)
            .andExpect(status().isNoContent());
    }

    @Test
    void testGetLobbyInformation() throws Exception {
        User admin = createTestUser("admin", 1l);
        User usr = createTestUser("user", 2l);
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(admin));
        lobby.addPlayer(LogicEntityMapper.createPlayerFromUser(usr));

        Mockito.when(userService.getUser(1l)).thenReturn(usr);
        Mockito.when(lobbyService.getLobbyById(1l)).thenReturn(lobby);
        doNothing().when(lobbyService).validateUserIsInLobby(usr, lobby);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1")
            .header("uid", 2);

        mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
            .andExpect(jsonPath("$.admin.id", is(admin.getId().intValue())))
            .andExpect(jsonPath(String.format("$.players[?(@.id == %d)]", usr.getId())).exists());
    }

    @Test
    void testGetLobbySseEmitterToken() throws Exception {
        User user = createTestUser("test", 1l);
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(user));

        Mockito.when(userService.getUser(1l)).thenReturn(user);
        Mockito.when(lobbyService.getLobbyById(1l)).thenReturn(lobby);
        doNothing().when(lobbyService).validateUserIsInLobby(user, lobby);
        Mockito.when(lobbyService.getLobbyEmitterToken(lobby)).thenReturn("token123");

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/sse")
            .header("uid", 1);

        mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(content().string("token123"));
    }

    @Test
    void testGetLobbySseEmitter() throws Exception {
        User user = createTestUser("test", 1l);
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(user));

        Mockito.when(lobbyService.getLobbyById(1l)).thenReturn(lobby);
        doNothing().when(lobbyService).validateLobbyEmitterToken(lobby, "token123");
        SseEmitter mockEmitter = mock(SseEmitter.class);
        Mockito.when(lobbyService.getLobbyEmitter(lobby)).thenReturn(mockEmitter);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/sse/token123");

        mockMvc.perform(getRequest)
            .andExpect(status().isOk());
        
    }

    @Test
    void testGetVoiceChannelToken() throws Exception{
        User user = createTestUser("test", 1l);
        Lobby lobby = new Lobby(1L, LogicEntityMapper.createPlayerFromUser(user));
        RTCTokenBuilder newtoken = new RTCTokenBuilder();
        String token = newtoken.buildTokenWithUserAccount(lobby.getId().toString(), user.getId().toString(), VoiceChatRole.Role_Publisher);
        Mockito.when(lobbyService.getLobbyById(1l)).thenReturn(lobby);
        Mockito.when(lobbyService.getLobbyVoiceToken(lobby)).thenReturn(token);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/channels");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }
}
