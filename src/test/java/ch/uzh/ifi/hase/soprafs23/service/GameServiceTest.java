package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PollGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PollOptionGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PollParticipantGetDTO;

public class GameServiceTest {
    GameService gameService = new GameService();

    private Lobby createValidMockLobby() {
        Lobby mockLobby = mock(Lobby.class);
        Mockito.when(mockLobby.getId()).thenReturn(1l);
        Mockito.when(mockLobby.getLobbySize()).thenReturn(Lobby.MIN_SIZE);
        return mockLobby;
    }

    @Test
    void testCreateNewGame() {
        Lobby lobby = createValidMockLobby();
        Game game = gameService.createNewGame(lobby);
        assertEquals(game, gameService.getGame(lobby));
    }

    @Test
    void testGetGame_notFound() {
        Lobby lobby = createValidMockLobby();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->gameService.getGame(lobby));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testStartGame() {
        Game game = mock(Game.class);
        gameService.startGame(game);
        verify(game).startGame();
    }

    @Test
    void testValidateGameStarted() {
        Game game = mock(Game.class);
        when(game.isStarted()).thenReturn(false);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gameService.validateGameStarted(game));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void testValidateParticipant() {
        Poll poll = mock(Poll.class);
        User user = mock(User.class);
        Player player = mock(Player.class);
        PollParticipant participant = mock(PollParticipant.class);
        when(poll.getPollParticipants()).thenReturn(List.of(participant));
        when(participant.getPlayer()).thenReturn(player);
        when(player.getId()).thenReturn(1l);
        when(user.getId()).thenReturn(1l);

        gameService.validateParticipant(poll, user);
    }

    @Test
    void testValidateParticipant_notParticipant() {
        Poll poll = mock(Poll.class);
        User user = mock(User.class);
        Player player = mock(Player.class);
        PollParticipant participant = mock(PollParticipant.class);
        when(poll.getPollParticipants()).thenReturn(List.of(participant));
        when(participant.getPlayer()).thenReturn(player);
        when(player.getId()).thenReturn(1l);
        when(user.getId()).thenReturn(2l);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->gameService.validateParticipant(poll, user));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void testCensorPollGetDTO() {
        PollGetDTO pollGetDTO = new PollGetDTO();

        pollGetDTO.setParticipants(List.of(mock(PollParticipantGetDTO.class)));
        pollGetDTO.setPollOptions(List.of(mock(PollOptionGetDTO.class)));

        gameService.censorPollGetDTO(pollGetDTO);

        assertEquals(Collections.emptyList(), pollGetDTO.getParticipants());
        assertEquals(Collections.emptyList(), pollGetDTO.getPollOptions());
    }

    @Test
    void testGetParticipant() {
        Poll poll = mock(Poll.class);
        User user = mock(User.class);
        Player player = mock(Player.class);
        PollParticipant expectedParticipant = mock(PollParticipant.class);
        when(poll.getPollParticipants()).thenReturn(List.of(expectedParticipant, mock(PollParticipant.class)));
        when(expectedParticipant.getPlayer()).thenReturn(player);
        when(player.getId()).thenReturn(1l);
        when(user.getId()).thenReturn(1l);

        assertEquals(expectedParticipant, gameService.getParticipant(poll, user));
    }

    @Test
    void testGetPollOption() {
        Poll poll = mock(Poll.class);
        PollOption expected = mock(PollOption.class);
        PollOption unexptedted = mock(PollOption.class);
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        when(expected.getPlayer()).thenReturn(p1);
        when(unexptedted.getPlayer()).thenReturn(p2);
        when(p1.getId()).thenReturn(1l);
        when(p2.getId()).thenReturn(2l);
        when(poll.getPollOptions()).thenReturn(List.of(expected, unexptedted));

        assertEquals(expected, gameService.getPollOption(poll, 1l));
    }

    @Test
    void testGetPollOption_notFound() {
        Poll poll = mock(Poll.class);
        PollOption expected = mock(PollOption.class);
        PollOption unexptedted = mock(PollOption.class);
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        when(expected.getPlayer()).thenReturn(p1);
        when(unexptedted.getPlayer()).thenReturn(p2);
        when(p1.getId()).thenReturn(1l);
        when(p2.getId()).thenReturn(2l);
        when(poll.getPollOptions()).thenReturn(List.of(expected, unexptedted));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->gameService.getPollOption(poll, 3l));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testCastVote() {
        Poll poll = mock(Poll.class);
        PollParticipant participant = mock(PollParticipant.class);
        PollOption option = mock(PollOption.class);

        gameService.castVote(poll, participant, option);
        verify(poll).castVote(participant, option);
    }

    @Test
    void testCastVote_illegalVote() {
        Poll poll = mock(Poll.class);
        PollParticipant participant = mock(PollParticipant.class);
        PollOption option = mock(PollOption.class);

        doThrow(new IllegalArgumentException("test")).when(poll).castVote(participant, option);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->gameService.castVote(poll, participant, option));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("test", exception.getReason());   
    }

    @Test
    void testRemoveVote() {
        Poll poll = mock(Poll.class);
        PollParticipant participant = mock(PollParticipant.class);
        PollOption option = mock(PollOption.class);

        gameService.removeVote(poll, participant, option);
        verify(poll).removeVote(participant, option);
    }

    @Test
    void testRemoveVote_illegalRemove() {
        Poll poll = mock(Poll.class);
        PollParticipant participant = mock(PollParticipant.class);
        PollOption option = mock(PollOption.class);

        doThrow(new IllegalArgumentException("test")).when(poll).removeVote(participant, option);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, ()->gameService.removeVote(poll, participant, option));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("test", exception.getReason());   
    }

    @Test
    void testOnGameEnd() {

    }
}
