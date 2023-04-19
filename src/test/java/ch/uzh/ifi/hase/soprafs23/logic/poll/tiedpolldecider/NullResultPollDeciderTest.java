package ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ch.uzh.ifi.hase.soprafs23.logic.poll.PlayerPoll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.NullPollCommand;

public class NullResultPollDeciderTest {
    NullResultPollDecider decider = new NullResultPollDecider();

    @Test
    void testExecuteTiePoll() {
        PlayerPoll poll = mock(PlayerPoll.class);

        decider.executeTiePoll(poll, List.of(), poll::finish);
        verify(poll).finish();
        verify(poll).setResultCommand(Mockito.any(NullPollCommand.class));
    }
}
