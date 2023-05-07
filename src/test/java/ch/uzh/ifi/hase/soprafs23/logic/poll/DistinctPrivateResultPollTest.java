package ch.uzh.ifi.hase.soprafs23.logic.poll;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.instantpollcommand.PrivateInstantPollCommand;

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

    @Test
    void testFinish() {
        PrivateResultPollOption pollOption1 = mock(PrivateResultPollOption.class);
        PrivateResultPollOption pollOption2 = mock(PrivateResultPollOption.class);
        PrivateResultPollOption pollOption3 = mock(PrivateResultPollOption.class);

        PrivateInstantPollCommand pollCommand1 = mock(PrivateInstantPollCommand.class);
        PrivateInstantPollCommand pollCommand2 = mock(PrivateInstantPollCommand.class);
        PrivateInstantPollCommand pollCommand3 = mock(PrivateInstantPollCommand.class);

        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);

        when(pollOption1.getSupportersAmount()).thenReturn(1);
        when(pollOption2.getSupportersAmount()).thenReturn(1);
        when(pollOption3.getSupportersAmount()).thenReturn(0);
        when(pollOption1.getPollCommand()).thenReturn(pollCommand1);
        when(pollOption2.getPollCommand()).thenReturn(pollCommand2);
        when(pollOption3.getPollCommand()).thenReturn(pollCommand3);
        when(pollCommand1.getInformationOwner()).thenReturn(p1);
        when(pollCommand2.getInformationOwner()).thenReturn(p2);

        DistinctPrivateResultPoll distinctPrivateResultPoll = new DistinctPrivateResultPoll(
            null, 
            "test", 
            List.of(pollOption1, pollOption2, pollOption3),
            mock(PollParticipant.class),
            0, 
            null);

        distinctPrivateResultPoll.finish();
        verify(pollCommand1).execute_instantly();
        verify(pollCommand2).execute_instantly();
        verify(p1).addPrivatePollCommand(pollCommand1);
        verify(p2).addPrivatePollCommand(pollCommand2);
    }

    @Test
    void testFinish_notEnoughVotes() {}
}
