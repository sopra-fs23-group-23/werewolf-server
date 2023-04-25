package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GameServiceIntegrationTest {
    GameService gameService = new GameService();

    @Test
    void testCreateGame() {
        Player player1 = new Player(1L, "Player1");
        Player player2 = new Player(2L, "Player2");
        Player player3 = new Player(3L, "Player3");
        Player player4 = new Player(4L, "Player4");
        Player player5 = new Player(5L, "Player5");

        Lobby lobby = new Lobby(1L, player1);
        lobby.addPlayer(player2);
        lobby.addPlayer(player3);
        lobby.addPlayer(player4);
        lobby.addPlayer(player5);
        lobby.instantiateRoles();

        Game game = new Game(lobby);

        gameService.createNewGame(lobby);

        assertEquals(gameService.getGame(lobby).getLobby().getAdmin().getId(), player1.getId());
        assertFalse(game.isFinished());
    }
}
