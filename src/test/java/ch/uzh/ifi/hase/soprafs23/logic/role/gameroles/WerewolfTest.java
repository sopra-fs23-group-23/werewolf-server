package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import static org.mockito.Mockito.mock;
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
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.KillPlayerPollCommand;

public class WerewolfTest {
    private Supplier<List<Player>> createMockAlivePlayersGetter (List<Player> expected) {
        return new Supplier<List<Player>>() {
            @Override
            public List<Player> get() {
                return expected;
            }
            
        };
    }

    @Test
    void testCreateNightPoll() {
        List<Player> expected = List.of(
            mock(Player.class),
            mock(Player.class),
            mock(Player.class),
            mock(Player.class)
        );
        List<Player> expectedWerewolves = List.of(
            expected.get(0),
            expected.get(2)
        );
        Werewolf werewolf = new Werewolf(createMockAlivePlayersGetter(expected));
        expectedWerewolves.stream().forEach(werewolf::addPlayer);

        Poll poll = werewolf.createNightPoll().get();

        assertThat(
            "Contains all alive players in any order as participants",
            poll.getPollParticipants().stream().map(PollParticipant::getPlayer).toList(),
            containsInAnyOrder(expectedWerewolves.toArray())
        );
        assertThat(
            "Contains all alive players in any order as options",
            poll.getPollOptions().stream().map(PollOption::getPlayer).toList(),
            containsInAnyOrder(expected.toArray())
        );
        assertTrue(poll.getPollOptions().stream().findFirst().get().getPollCommand() instanceof KillPlayerPollCommand);
    }
}
