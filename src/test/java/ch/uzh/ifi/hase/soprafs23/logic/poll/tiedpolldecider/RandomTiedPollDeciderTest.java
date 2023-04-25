package ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

public class RandomTiedPollDeciderTest {
    @Test
    void testExecuteTiePoll() {
        RandomTiedPollDecider randomTiedPollDecider = new RandomTiedPollDecider();
        Poll poll = mock(Poll.class);
        PollCommand expectedCommand = mock(PollCommand.class);
        PollOption pollOption1 = mock(PollOption.class);
        PollOption pollOption2 = mock(PollOption.class);
        PollOption pollOption3 = mock(PollOption.class);
        when(pollOption1.getPollCommand()).thenReturn(expectedCommand);
        when(pollOption2.getPollCommand()).thenReturn(expectedCommand);
        when(pollOption3.getPollCommand()).thenReturn(expectedCommand);
        List<PollOption> pollOptions = List.of(pollOption1, pollOption2, pollOption3);
        randomTiedPollDecider.executeTiePoll(poll, pollOptions, () -> {
            verify(poll).setResultCommand(expectedCommand);
        });
    }
}
