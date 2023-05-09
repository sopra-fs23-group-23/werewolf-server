package ch.uzh.ifi.hase.soprafs23.logic.poll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.NullPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PrivatePollCommand;

public class PrivateResultPollTest {
    private List<PrivateResultPollOption> createMockPollOptions() {
        PrivateResultPollOption pollOption1 = mock(PrivateResultPollOption.class);
        PrivateResultPollOption pollOption2 = mock(PrivateResultPollOption.class);
        PrivateResultPollOption pollOption3 = mock(PrivateResultPollOption.class);
        when(pollOption1.getSupportersAmount()).thenReturn(1);
        when(pollOption2.getSupportersAmount()).thenReturn(2);
        when(pollOption3.getSupportersAmount()).thenReturn(3);

        when(pollOption1.getPollCommand()).thenReturn(mock(PrivatePollCommand.class));
        when(pollOption2.getPollCommand()).thenReturn(mock(PrivatePollCommand.class));
        when(pollOption3.getPollCommand()).thenReturn(mock(PrivatePollCommand.class));
        return List.of(pollOption1, pollOption2, pollOption3);
    }

    @Test
    void testFinish() {
        List<PrivateResultPollOption> pollOptions = createMockPollOptions();
        PrivateResultPoll privateResultPoll = new PrivateResultPoll(
            null, 
            "test", 
            pollOptions,
            List.of(),
            0, 
            null);
        PrivatePollCommand expectedResultCommand = (PrivatePollCommand)pollOptions.get(2).getPollCommand();
        Player player = mock(Player.class);
        when(expectedResultCommand.getInformationOwner()).thenReturn(player);
        privateResultPoll.finish();

        assertEquals(NullPollCommand.class, privateResultPoll.getResultCommand().getClass());
        verify(player).addPrivatePollCommand(expectedResultCommand);
    }
}
