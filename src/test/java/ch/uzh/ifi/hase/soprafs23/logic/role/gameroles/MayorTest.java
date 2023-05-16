package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ch.uzh.ifi.hase.soprafs23.logic.game.Scheduler;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.AddPlayerToRolePollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;

public class MayorTest {

    @Test
    void testExecuteTiePoll_noMayor() {
        TiedPollDecider tiedPollDecider = mock(TiedPollDecider.class);
        Mayor mayor = new Mayor(0, null, tiedPollDecider, null);

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
        Mayor mayor = new Mayor(0, null, tiedPollDecider, scheduler);
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
        assertEquals(Date.class, poll.getScheduledFinish().getClass());
        verify(scheduler).schedule(Mockito.any(Runnable.class), Mockito.anyInt());
    }

    @Test
    void testAddPlayer() {
        Mayor mayor = new Mayor(0, null, null, null);
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        mayor.addPlayer(p1);
        assertEquals(1, mayor.getPlayers().size());
        assertEquals(p1, mayor.getPlayers().get(0));
        mayor.addPlayer(p2);
        assertEquals(1, mayor.getPlayers().size());
        assertEquals(p2, mayor.getPlayers().get(0));
    }

    @Test
    void testCreateNightPoll_MayorAlive() {
        Mayor mayor = new Mayor(0, null, null, null);
        Player p1 = mock(Player.class);
        mayor.addPlayer(p1);
        Optional<Poll> poll = mayor.createNightPoll();
        assertEquals(Optional.empty(), poll);
    }

    @Test
    void testCreateDayPoll_MayorAlive() {
        Mayor mayor = new Mayor(0, null, null, null);
        Player p1 = mock(Player.class);
        mayor.addPlayer(p1);
        Optional<Poll> poll = mayor.createDayPoll();
        assertEquals(Optional.empty(), poll);
    }

    private List<Player> getPlayers() {
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        Player p3 = mock(Player.class);
        return List.of(p1, p2, p3);
    }

    private void checkMayorKilledPoll(Optional<Poll> poll, Player p1) {
        assertTrue(poll.isPresent());
        assertEquals(1, poll.get().getPollParticipants().size());
        assertEquals(p1, poll.get().getPollParticipants().stream().findFirst().get().getPlayer());
        assertEquals(3, poll.get().getPollOptions().size());
        assertTrue(poll.get().getPollOptions().stream().findFirst().get().getPollCommand() instanceof AddPlayerToRolePollCommand);
    }

    @Test
    void testCreateNightPoll_MayorDead() {
        TiedPollDecider tiedPollDecider = mock(TiedPollDecider.class);
        Mayor mayor = new Mayor(0, this::getPlayers, tiedPollDecider, null);
        Player p1 = mock(Player.class);
        mayor.addPlayer(p1);
        mayor.onPlayerKilled_Unrevivable(p1);
        Optional<Poll> poll = mayor.createNightPoll();
        checkMayorKilledPoll(poll, p1);
    }

    @Test
    void testCreateDayPoll_MayorDead() {
        TiedPollDecider tiedPollDecider = mock(TiedPollDecider.class);
        Mayor mayor = new Mayor(0, this::getPlayers, tiedPollDecider, null);
        Player p1 = mock(Player.class);
        mayor.addPlayer(p1);
        mayor.onPlayerKilled_Unrevivable(p1);
        Optional<Poll> poll = mayor.createDayPoll();
        checkMayorKilledPoll(poll, p1);
    }
}
