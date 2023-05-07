package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public class LoverTest {
    private Player createMockPlayer() {
        Player player = mock(Player.class);
        when(player.isAlive()).thenReturn(true);
        return player;
    }

    private Supplier<List<Player>> createMockAlivePlayersGetter (List<Player> expected) {
        return new Supplier<List<Player>>() {
            @Override
            public List<Player> get() {
                return expected.stream().filter(Player::isAlive).toList();
            }
            
        };
    }

    @Test
    void testHasWon() {
        List<Player> expected = List.of(
            createMockPlayer(),
            createMockPlayer(),
            createMockPlayer()
        );
        Lover lover = new Lover(createMockAlivePlayersGetter(expected));
        lover.addPlayer(expected.get(0));
        lover.addPlayer(expected.get(1));
        assertFalse(lover.hasWon());
        when(expected.get(2).isAlive()).thenReturn(false);
        assertTrue(lover.hasWon());
    }

    @Test
    void testOnPlayerKilled() {
        Lover lover = new Lover(null);
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        lover.addPlayer(p1);
        lover.addPlayer(p2);
        lover.onPlayerKilled();
        verify(p1).killPlayer();
        verify(p1).killPlayer();
    }
}
