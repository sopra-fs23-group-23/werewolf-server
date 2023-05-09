package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.KillPlayerPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

public class LoverTest {
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
