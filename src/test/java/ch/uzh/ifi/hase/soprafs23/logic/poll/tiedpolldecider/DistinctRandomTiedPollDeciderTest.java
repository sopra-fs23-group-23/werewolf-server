package ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.DistinctPrivateResultPoll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

public class DistinctRandomTiedPollDeciderTest {
    DistinctPrivateResultPoll poll = mock(DistinctPrivateResultPoll.class);
    PollParticipant pollParticipant = new PollParticipant(mock(Player.class), 2);
    List<PollOption> pollOptions = generateMockPollOptions();

    private List<PollOption> generateMockPollOptions() {
        PollOption pollOption1 = new PollOption(mock(Player.class), mock(PollCommand.class));
        PollOption pollOption2 = new PollOption(mock(Player.class), mock(PollCommand.class));
        PollOption pollOption3 = new PollOption(mock(Player.class), mock(PollCommand.class));
        PollOption pollOption4 = new PollOption(mock(Player.class), mock(PollCommand.class));
        PollOption pollOption5 = new PollOption(mock(Player.class), mock(PollCommand.class));
        return List.of(pollOption1, pollOption2, pollOption3, pollOption4, pollOption5);
    }

    @BeforeEach
    void setup() {
        when(poll.getPollParticipants()).thenReturn(List.of(pollParticipant));
        when(poll.getPollOptions()).thenReturn(pollOptions);
        doAnswer(invocation -> {
            PollParticipant participant = invocation.getArgument(0);
            PollOption pollOption = invocation.getArgument(1);

            pollOption.addSupporter(participant);
            participant.decreaseRemainingVotes();
            return null;
        }).when(poll).castVote(Mockito.any(PollParticipant.class), Mockito.any(PollOption.class));
    }

    @Test
    void testExecuteTiePoll_noneSelected() {
        DistinctRandomTiedPollDecider distinctRandomTiedPollDecider = new DistinctRandomTiedPollDecider();
        distinctRandomTiedPollDecider.executeTiePoll(poll, List.of(), poll::finish);

        verify(poll).finish();
        List<PollOption> selected = pollOptions.stream().filter(option -> option.getSupportersAmount()==1).toList();
        assertEquals(2, selected.size());
        assertNotEquals(selected.get(0), selected.get(1), "The selected options should be different");
    }

    @Test
    void testExecuteTiePoll_oneSelected() {
        PollOption pollOption2 = pollOptions.get(1);
        pollOption2.addSupporter(pollParticipant);
        pollParticipant.decreaseRemainingVotes();

        DistinctRandomTiedPollDecider distinctRandomTiedPollDecider = new DistinctRandomTiedPollDecider();
        distinctRandomTiedPollDecider.executeTiePoll(poll, List.of(pollOption2), poll::finish);

        verify(poll).finish();
        List<PollOption> selected = pollOptions.stream().filter(option -> option.getSupportersAmount()==1).toList();
        assertEquals(2, selected.size());
        assertNotEquals(selected.get(0), selected.get(1), "The selected options should be different");
        assertTrue(selected.contains(pollOption2), "The selected options should contain the already selected option");
    }
}
