package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.AddPlayerToRolePollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.KillPlayerPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;

public class VillagerTest {
    private Supplier<List<Player>> createMockAlivePlayersGetter (List<Player> expected) {
        return new Supplier<List<Player>>() {
            @Override
            public List<Player> get() {
                return expected.stream().filter(Player::isAlive).toList();
            }
            
        };
    }

    private Player createMockPlayer() {
        Player player = mock(Player.class);
        when(player.isAlive()).thenReturn(true);
        return player;
    }

    private List<Player> getAlivePlayers() {
        return List.of(
            createMockPlayer(),
            createMockPlayer(),
            createMockPlayer(),
            createMockPlayer()
        );
    }

    @Test
    void testCreateDayPoll() {
        List<Player> expected = getAlivePlayers();
        TiedPollDecider tiedPollDecider = mock(TiedPollDecider.class);
        Villager villager = new Villager(null, createMockAlivePlayersGetter(expected), tiedPollDecider);
        Poll poll = villager.createDayPoll().get();
        assertThat(
            "Contains all alive players in any order as participants",
            poll.getPollParticipants().stream().map(PollParticipant::getPlayer).toList(),
            containsInAnyOrder(expected.toArray())
        );
        assertThat(
            "Contains all alive players in any order as options",
            poll.getPollOptions().stream().map(PollOption::getPlayer).toList(),
            containsInAnyOrder(expected.toArray())
        );
        assertTrue(poll.getPollOptions().stream().findFirst().get().getPollCommand() instanceof KillPlayerPollCommand);
    }

    @Test
    void testCreateFirstDayPoll() {
        List<Player> expected = getAlivePlayers();
        TiedPollDecider tiedPollDecider = mock(TiedPollDecider.class);
        Villager villager = new Villager(null, createMockAlivePlayersGetter(expected), tiedPollDecider);
        Poll poll = villager.createFirstDayPoll().get();
        assertThat(
            "Contains all alive players in any order as participants",
            poll.getPollParticipants().stream().map(PollParticipant::getPlayer).toList(),
            containsInAnyOrder(expected.toArray())
        );
        assertThat(
            "Contains all alive players in any order as options",
            poll.getPollOptions().stream().map(PollOption::getPlayer).toList(),
            containsInAnyOrder(expected.toArray())
        );
        assertTrue(poll.getPollOptions().stream().findFirst().get().getPollCommand() instanceof AddPlayerToRolePollCommand);
    }
}
