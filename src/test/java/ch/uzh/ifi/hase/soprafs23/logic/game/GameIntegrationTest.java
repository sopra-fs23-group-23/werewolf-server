package ch.uzh.ifi.hase.soprafs23.logic.game;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class GameIntegrationTest {
    /*private Game game = new Game(createValidMockLobby());

    private Lobby createValidMockLobby() {
        Lobby mockLobby = mock(Lobby.class);
        Mockito.when(mockLobby.getId()).thenReturn(1L);
        Mockito.when(mockLobby.getLobbySize()).thenReturn(Lobby.MIN_SIZE);
        return mockLobby;
    }*/

    @Test
    public void testOnStageFinished_true() {
        /*Player player = new Player(1L, "TestPlayer");
        Lobby lobby = new Lobby(1L, player);
        Game game = new Game(lobby);*/

    }

    @Test
    public void testOnStageFinished_false() {

    }
}
