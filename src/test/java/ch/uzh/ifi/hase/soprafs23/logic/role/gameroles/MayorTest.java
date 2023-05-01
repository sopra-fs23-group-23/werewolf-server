package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ch.uzh.ifi.hase.soprafs23.logic.game.Scheduler;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;

public class MayorTest {

    @Test
    void testExecuteTiePoll_noMayor() {
        TiedPollDecider tiedPollDecider = mock(TiedPollDecider.class);
        Mayor mayor = new Mayor(null, tiedPollDecider, null);

        Poll poll = mock(Poll.class);
        List<PollOption> pollOptions = List.of();
        Runnable onTiePollFinished = mock(Runnable.class);
        mayor.executeTiePoll(poll, pollOptions, onTiePollFinished);
        verify(tiedPollDecider).executeTiePoll(poll, pollOptions, onTiePollFinished);
    }

    @Test
    void testExecuteTiePoll() {
        TiedPollDecider tiedPollDecider = mock(TiedPollDecider.class);
        Scheduler scheduler = mock(Scheduler.class);
        Mayor mayor = new Mayor(null, tiedPollDecider, scheduler);
        Player p1 = mock(Player.class);
        mayor.addPlayer(p1);
        List<PollOption> pollOptions = List.of(mock(PollOption.class), mock(PollOption.class));

        Poll poll = new Poll(Villager.class, "null", List.of(), List.of(), 10, mayor);

        mayor.executeTiePoll(poll, pollOptions, poll::finish);
        assertThat(
            "List equality without order",
            poll.getPollParticipants().stream().map(PollParticipant::getPlayer).toList(),
            containsInAnyOrder(Arrays.array(p1)));
        assertEquals(pollOptions, poll.getPollOptions());
        verify(scheduler).schedule(Mockito.any(Runnable.class), Mockito.anyInt());
        

    }
}
