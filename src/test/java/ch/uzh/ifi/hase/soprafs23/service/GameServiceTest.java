package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.service.wrapper.GameEmitter;

public class GameServiceTest {
    GameService gameService = new GameService();

    private Lobby createValidMockLobby() {
        Lobby mockLobby = mock(Lobby.class);
        Mockito.when(mockLobby.getId()).thenReturn(1l);
        Mockito.when(mockLobby.getLobbySize()).thenReturn(Lobby.MIN_SIZE);
        return mockLobby;
    }

    private Game createValidMockGame() {
        Game mockGame = mock(Game.class);
        Lobby mockLobby = createValidMockLobby();
        Mockito.when(mockGame.getLobby()).thenReturn(mockLobby);
        return mockGame;
    }

    @Test
    void testCreateGameEmitter() {
        Game game = createValidMockGame();
        GameEmitter emitter = gameService.createGameEmitter(game);
        assertEquals(emitter, gameService.getGameEmitter(game));
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
        verify(game).addObserver(gameService);
    }
}
