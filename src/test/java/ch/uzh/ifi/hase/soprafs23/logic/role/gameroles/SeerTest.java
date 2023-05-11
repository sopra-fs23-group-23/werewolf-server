package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PrivateResultPoll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PrivateRevealRolesNotificationPollCommand;

public class SeerTest {
    private class MockFunctions {
        private List<Player> players;

        public MockFunctions(List<Player> players) {
            this.players = players;
        }

        public List<Player> getAlivePlayers() {
            return players;
        }
    }

    @Test
    void testCreateNightPoll() {
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        when(p1.isAlive()).thenReturn(true);
        when(p2.isAlive()).thenReturn(true);

        MockFunctions mockFunctions = new MockFunctions(List.of(p1, p2));

        Seer seer = new Seer(mockFunctions::getAlivePlayers, null);
        seer.addPlayer(p1);
        Optional<Poll> poll = seer.createNightPoll();
        assertTrue(poll.isPresent());
        assertTrue(poll.get() instanceof PrivateResultPoll);
        assertTrue(poll.get().getPollOptions().size() == 2);
        assertEquals(p1, poll.get().getPollParticipants().stream().findFirst().get().getPlayer());
        assertTrue(poll.get().getPollOptions().stream().findFirst().get().getPollCommand() instanceof PrivateRevealRolesNotificationPollCommand);
    }

    @Test
    void testCreateNightPoll_SeerDead() {
        Player p1 = mock(Player.class);
        when(p1.isAlive()).thenReturn(false);
        Seer seer = new Seer(null, null);
        seer.addPlayer(p1);
        Optional<Poll> poll = seer.createNightPoll();
        assertTrue(poll.isEmpty());
    }
}
