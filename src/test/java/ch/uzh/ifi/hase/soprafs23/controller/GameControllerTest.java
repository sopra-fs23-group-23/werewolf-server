package ch.uzh.ifi.hase.soprafs23.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ch.uzh.ifi.hase.soprafs23.service.UserService.USERAUTH_HEADER;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PollGetDTO;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private UserService userService;

    @MockBean
    private GameService gameService;

    private User user = mock(User.class);
    private Lobby lobby = mock(Lobby.class);
    private Game game = mock(Game.class);
    
    @Test
    void testCreateNewGame() throws Exception {
        Mockito.when(userService.getUserByToken("token")).thenReturn(user);
        Mockito.when(lobbyService.getLobbyById(1l)).thenReturn(lobby);
        Mockito.when(gameService.createNewGame(lobby)).thenReturn(game);

        MockHttpServletRequestBuilder postRequest = post("/games/1")
            .header(USERAUTH_HEADER, "token");

        mockMvc.perform(postRequest)
            .andExpect(status().isCreated());
        
        verify(lobbyService).validateUserIsAdmin(user, lobby);
        verify(lobbyService).validateLobbySize(lobby);
        verify(lobbyService).assignRoles(lobby);
    }

    @Test
    void testGetGame() throws Exception {
        Mockito.when(userService.getUserByToken("token")).thenReturn(user);
        Mockito.when(lobbyService.getLobbyById(1l)).thenReturn(lobby);
        Mockito.when(gameService.getGame(lobby)).thenReturn(game);

        MockHttpServletRequestBuilder getRequest = get("/games/1")
            .header(USERAUTH_HEADER, "token");

        mockMvc.perform(getRequest)
            .andExpect(status().isOk());

        verify(lobbyService).validateUserIsInLobby(user, lobby);
        verify(gameService).toGameGetDTO(game);
    }

    @Test
    void testGetPoll() throws Exception {
        // Test GameController getPoll
        Poll poll = mock(Poll.class);
        Mockito.when(userService.getUserByToken("token")).thenReturn(user);
        Mockito.when(lobbyService.getLobbyById(1l)).thenReturn(lobby);
        Mockito.when(gameService.getGame(lobby)).thenReturn(game);
        Mockito.when(gameService.getCurrentPoll(game)).thenReturn(poll);
        Mockito.when(gameService.isPollParticipant(poll, user)).thenReturn(true);

        MockHttpServletRequestBuilder getRequest = get("/games/1/polls")
            .header(USERAUTH_HEADER, "token");

        mockMvc.perform(getRequest)
            .andExpect(status().isOk());

        verify(gameService).toPollGetDTO(poll);
        verify(gameService, never()).censorPollGetDTO(Mockito.any(PollGetDTO.class));
    }

    @Test
    void testGetPoll_nonParticipant() throws Exception {
        // Test GameController getPoll
        Poll poll = mock(Poll.class);
        Mockito.when(userService.getUserByToken("token")).thenReturn(user);
        Mockito.when(lobbyService.getLobbyById(1l)).thenReturn(lobby);
        Mockito.when(gameService.getGame(lobby)).thenReturn(game);
        Mockito.when(gameService.getCurrentPoll(game)).thenReturn(poll);
        Mockito.when(gameService.isPollParticipant(poll, user)).thenReturn(false);

        MockHttpServletRequestBuilder getRequest = get("/games/1/polls")
            .header(USERAUTH_HEADER, "token");

        mockMvc.perform(getRequest)
            .andExpect(status().isOk());

        verify(gameService).toPollGetDTO(poll);
        verify(gameService).censorPollGetDTO(Mockito.any());
    }

    @Test
    void testVote() throws Exception {
        // Test GameController vote
        Mockito.when(userService.getUserByToken("token")).thenReturn(user);
        Mockito.when(lobbyService.getLobbyById(1l)).thenReturn(lobby);
        Mockito.when(gameService.getGame(lobby)).thenReturn(game);

        Poll poll = mock(Poll.class);
        PollParticipant participant = mock(PollParticipant.class);
        PollOption option = mock(PollOption.class);

        Mockito.when(gameService.getCurrentPoll(game)).thenReturn(poll);
        Mockito.when(gameService.getParticipant(poll, user)).thenReturn(participant);
        Mockito.when(gameService.getPollOption(poll, 1l)).thenReturn(option);

        MockHttpServletRequestBuilder postRequest = put("/games/1/votes/1")
            .header(USERAUTH_HEADER, "token");

        mockMvc.perform(postRequest)
            .andExpect(status().isNoContent());

        verify(gameService).validateParticipant(poll, user);
        verify(gameService).castVote(poll, participant, option);
    }

    @Test
    void testRemoveVote() throws Exception {
        // Test GameController removeVote
        Mockito.when(userService.getUserByToken("token")).thenReturn(user);
        Mockito.when(lobbyService.getLobbyById(1l)).thenReturn(lobby);
        Mockito.when(gameService.getGame(lobby)).thenReturn(game);

        Poll poll = mock(Poll.class);
        PollParticipant participant = mock(PollParticipant.class);
        PollOption option = mock(PollOption.class);

        Mockito.when(gameService.getCurrentPoll(game)).thenReturn(poll);
        Mockito.when(gameService.getParticipant(poll, user)).thenReturn(participant);
        Mockito.when(gameService.getPollOption(poll, 1l)).thenReturn(option);

        MockHttpServletRequestBuilder deleteRequest = delete("/games/1/votes/1")
            .header(USERAUTH_HEADER, "token");

        mockMvc.perform(deleteRequest)
            .andExpect(status().isNoContent());

        verify(gameService).validateParticipant(poll, user);
        verify(gameService).removeVote(poll, participant, option);
    }
}
