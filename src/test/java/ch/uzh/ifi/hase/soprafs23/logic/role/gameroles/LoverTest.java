package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.KillPlayerPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

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
        Lover lover = new Lover(createMockAlivePlayersGetter(expected), null);
        lover.addPlayer(expected.get(0));
        lover.addPlayer(expected.get(1));
        assertFalse(lover.hasWon());
        when(expected.get(2).isAlive()).thenReturn(false);
        assertTrue(lover.hasWon());
    }

    private void mockPollCommandConsumer (PollCommand command) {
        assertTrue(command instanceof KillPlayerPollCommand);
        command.execute();
    }

    @Test
    void testOnPlayerKilled() {
        Lover lover = new Lover(null, this::mockPollCommandConsumer);
        Player p1 = new Player(1L, "Lover1");
        Player p2 = new Player(1L, "Lover2");
        lover.addPlayer(p1);
        lover.addPlayer(p2);
        p1.killPlayer();
        p1.setDeadPlayerUnrevivable();
        assertFalse(p2.isAlive(), "Lover2 should be dead after killing lover1");
    }
}
