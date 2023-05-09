package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.NullResultPollDecider;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HunterTest {
    private Supplier<List<Player>> createMockAlivePlayersGetter (List<Player> expected) {
        return new Supplier<List<Player>>() {
            @Override
            public List<Player> get() {
                return expected.stream().filter(Player::isAlive).toList();
            }
        };
    }

    private List<Player> getPlayers() {
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        Player p3 = mock(Player.class);
        return List.of(p1, p2, p3);
    }

    private void checkHunterKilledPoll(Optional<Poll> poll, Player p1) {
        assertTrue(poll.isPresent());
        assertEquals(1, poll.get().getPollParticipants().size());
        assertEquals(p1, poll.get().getPollParticipants().stream().findFirst().get().getPlayer());
        assertEquals(3, poll.get().getPollOptions().size());
    }

    @Test
    void testCreateDayPoll_HunterDead() {
        Hunter hunter = new Hunter(null, this::getPlayers);
        Player p1 = mock(Player.class);
        hunter.addPlayer(p1);
        hunter.onPlayerKilled();
        Optional<Poll> poll = hunter.createDayPoll();
        checkHunterKilledPoll(poll, p1);
    }

    @Test
    void testCreateNightPoll_HunterDead() {
        Hunter hunter = new Hunter(null, this::getPlayers);
        Player p1 = mock(Player.class);
        hunter.addPlayer(p1);
        hunter.onPlayerKilled();
        Optional<Poll> poll = hunter.createNightPoll();
        checkHunterKilledPoll(poll, p1);
    }

    @Test
    void testAddPlayer() {
        Hunter hunter = new Hunter(null, null);
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        hunter.addPlayer(p1);
        assertEquals(1, hunter.getPlayers().size());
        assertEquals(p1, hunter.getPlayers().get(0));
        hunter.addPlayer(p2);
        assertEquals(1, hunter.getPlayers().size());
        assertEquals(p2, hunter.getPlayers().get(0));
    }
}
