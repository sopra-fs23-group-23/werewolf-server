package ch.uzh.ifi.hase.soprafs23.service.wrapper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public class PlayerEmitterTest {
    private Game createMockGame() {
        Lobby lobby = mock(Lobby.class);
        Game game = mock(Game.class);
        Player p1 = new Player(1l, "test");
        Player p2 = new Player(2l, "test2");
        Mockito.when(lobby.getPlayers()).thenReturn(Arrays.asList(p1,p2));
        Mockito.when(game.getLobby()).thenReturn(lobby);
        return game;
    }

    @Test
    void testForAllPlayerEmitters() {
        Game game = createMockGame();
        ArrayList<SseEmitter> emitters = new ArrayList<>();

        Consumer<SseEmitter> mockConsumer = new Consumer<SseEmitter>() {
            @Override
            public void accept(SseEmitter arg0) {
                emitters.add(arg0);
            }
        };

        PlayerEmitter emitter = new PlayerEmitter(game);
        emitter.forAllPlayerEmitters(mockConsumer);
        SseEmitter[] expected = {emitter.getPlayerEmitter(1l), emitter.getPlayerEmitter(2l)};
        assertThat("List equality without order", emitters, containsInAnyOrder(expected));
    }

    @Test
    void testGetPlayerEmitter() {
        Game game = createMockGame();

        PlayerEmitter emitter = new PlayerEmitter(game);
        assertTrue(emitter.getPlayerEmitter(1l) instanceof SseEmitter, "Playeremitter of p1 is not instance of SseEmitter");
        assertTrue(emitter.getPlayerEmitter(2l) instanceof SseEmitter, "Playeremitter of p2 is not instance of SseEmitter");
    }
}
