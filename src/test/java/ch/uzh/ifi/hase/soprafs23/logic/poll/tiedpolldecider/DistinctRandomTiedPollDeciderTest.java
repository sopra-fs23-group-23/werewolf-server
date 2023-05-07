package ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.DistinctPrivateResultPoll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

public class DistinctRandomTiedPollDeciderTest {
    private List<PollOption> generateMockPollOptions() {
        PollOption pollOption1 = new PollOption(mock(Player.class), mock(PollCommand.class));
        PollOption pollOption2 = new PollOption(mock(Player.class), mock(PollCommand.class));
        PollOption pollOption3 = new PollOption(mock(Player.class), mock(PollCommand.class));
        PollOption pollOption4 = new PollOption(mock(Player.class), mock(PollCommand.class));
        PollOption pollOption5 = new PollOption(mock(Player.class), mock(PollCommand.class));
        return List.of(pollOption1, pollOption2, pollOption3, pollOption4, pollOption5);
    }

    @Test
    void testExecuteTiePoll_noneSelected() {
        DistinctPrivateResultPoll poll = mock(DistinctPrivateResultPoll.class);
        PollParticipant pollParticipant = mock(PollParticipant.class);
        List<PollOption> pollOptions = generateMockPollOptions();

        when(poll.getPollParticipants()).thenReturn(List.of(pollParticipant));
        when(poll.getPollOptions()).thenReturn(pollOptions);
        when(pollParticipant.getRemainingVotes()).thenReturn(2);

        DistinctRandomTiedPollDecider distinctRandomTiedPollDecider = new DistinctRandomTiedPollDecider();
        distinctRandomTiedPollDecider.executeTiePoll(poll, List.of(), poll::finish);

        verify(poll).finish();
        List<PollOption> selected = pollOptions.stream().filter(option -> option.getSupportersAmount()==1).toList();
        assertEquals(2, selected.size());
        assertFalse(selected.get(0).equals(selected.get(1)), "The selected options should be different");
    }

    @Test
    void testExecuteTiePoll_oneSelected() {
        DistinctPrivateResultPoll poll = mock(DistinctPrivateResultPoll.class);
        PollParticipant pollParticipant = mock(PollParticipant.class);
        List<PollOption> pollOptions = generateMockPollOptions();

        when(poll.getPollParticipants()).thenReturn(List.of(pollParticipant));
        when(poll.getPollOptions()).thenReturn(pollOptions);
        when(pollParticipant.getRemainingVotes()).thenReturn(2);

        PollOption pollOption2 = pollOptions.get(1);
        pollOption2.addSupporter(pollParticipant);

        DistinctRandomTiedPollDecider distinctRandomTiedPollDecider = new DistinctRandomTiedPollDecider();
        distinctRandomTiedPollDecider.executeTiePoll(poll, List.of(pollOption2), poll::finish);

        verify(poll).finish();
        List<PollOption> selected = pollOptions.stream().filter(option -> option.getSupportersAmount()==1).toList();
        assertEquals(2, selected.size());
        assertFalse(selected.get(0).equals(selected.get(1)), "The selected options should be different");
        assertTrue(selected.contains(pollOption2), "The selected options should contain the already selected option");
    }
}
