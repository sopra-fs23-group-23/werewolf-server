package ch.uzh.ifi.hase.soprafs23.logic.poll;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

public class DistinctPrivateResultPollTest {
    @Test
    void testCastVote_sameOption() {
        DistinctPrivateResultPoll distinctPrivateResultPoll = new DistinctPrivateResultPoll(
            null, 
            "test", 
            List.of(),
            mock(PollParticipant.class),
            0, 
            null);

        PollOption pollOption = mock(PollOption.class);
        PollParticipant pollParticipant = mock(PollParticipant.class);

        when(pollOption.getSupporters()).thenReturn(List.of(pollParticipant));

        assertThrows(IllegalArgumentException.class, () -> distinctPrivateResultPoll.castVote(pollParticipant, pollOption));
    }
}
