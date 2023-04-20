package ch.uzh.ifi.hase.soprafs23.logic.poll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;

public class PlayerPollTest {
    
    private PlayerPoll setupBasicPlayerPoll () {
        List<PollOption> mockOptions = List.of(
            mock(PollOption.class),
            mock(PollOption.class),
            mock(PollOption.class)
        );
        List<PollParticipant> mockParticipants = List.of(
            mock(PollParticipant.class),
            mock(PollParticipant.class)
        );
        TiedPollDecider mockTiedPollDecider = mock(TiedPollDecider.class);
        return new PlayerPoll("", mockOptions, mockParticipants, 0, mockTiedPollDecider);
    }

    @Test
    void testCastVote() {
        PlayerPoll poll = setupBasicPlayerPoll();
        PollParticipant p1 = poll.getPollParticipants().iterator().next();
        when(p1.getRemainingVotes()).thenReturn(1);
        PollOption o1 = poll.getPollOptions().iterator().next();
        poll.castVote(p1, o1);
        verify(p1).decreaseRemainingVotes();
        verify(o1).addSupporter(p1);
    }

    @Test
    void testCastVote_noVotes() {
        PlayerPoll poll = setupBasicPlayerPoll();
        PollParticipant p1 = poll.getPollParticipants().iterator().next();
        when(p1.getRemainingVotes()).thenReturn(0);
        PollOption o1 = poll.getPollOptions().iterator().next();
        assertThrows(IllegalArgumentException.class, ()-> poll.castVote(p1, o1));
    }

    @Test
    void testRemoveVote() {
        PlayerPoll poll = setupBasicPlayerPoll();
        PollParticipant p1 = poll.getPollParticipants().iterator().next();
        PollOption o1 = poll.getPollOptions().iterator().next();
        when(p1.getRemainingVotes()).thenReturn(0);
        when(o1.getSupporters()).thenReturn(List.of(p1));
        poll.removeVote(p1, o1);
        verify(p1).increaseRemainingVotes();
        verify(o1).removeSupporter(p1);
    }

    @Test
    void testRemoveVote_noSupporter() {
        PlayerPoll poll = setupBasicPlayerPoll();
        PollParticipant p1 = poll.getPollParticipants().iterator().next();
        PollOption o1 = poll.getPollOptions().iterator().next();
        when(p1.getRemainingVotes()).thenReturn(0);
        when(o1.getSupporters()).thenReturn(List.of());
        assertThrows(IllegalArgumentException.class, ()-> poll.removeVote(p1, o1));
    }

    @Test
    void testFinishPoll() {
        // Test that the poll finishes if there is only one option
        PlayerPoll poll = setupBasicPlayerPoll();
        PollOption o1 = poll.getPollOptions().iterator().next();
        PollOption o2 = poll.getPollOptions().stream().skip(1).findFirst().get();
        PollOption o3 = poll.getPollOptions().stream().skip(2).findFirst().get();
        when(o1.getSupportersAmount()).thenReturn(1);
        when(o2.getSupportersAmount()).thenReturn(3);
        when(o3.getSupportersAmount()).thenReturn(2);
        PollCommand expectedCommand = mock(PollCommand.class);
        PollCommand wrongCommand = mock(PollCommand.class);
        when(o1.getPollCommand()).thenReturn(wrongCommand);
        when(o2.getPollCommand()).thenReturn(expectedCommand);
        when(o3.getPollCommand()).thenReturn(wrongCommand);
        poll.finish();
        assertEquals(expectedCommand, poll.getResultCommand());
    }

    @Test
    void testStartPoll_tiedPoll() {
        // Test that the poll finishes if there is a tie
        List<PollOption> mockOptions = List.of(
            mock(PollOption.class),
            mock(PollOption.class),
            mock(PollOption.class)
        );
        List<PollParticipant> mockParticipants = List.of(
            mock(PollParticipant.class),
            mock(PollParticipant.class)
        );
        PollOption o1 = mockOptions.get(0);
        PollOption o2 = mockOptions.get(1);
        PollOption o3 = mockOptions.get(2);
        when(o1.getSupportersAmount()).thenReturn(1);
        when(o2.getSupportersAmount()).thenReturn(2);
        when(o3.getSupportersAmount()).thenReturn(2);
        PollCommand expectedCommand = mock(PollCommand.class);
        PollCommand wrongCommand = mock(PollCommand.class);
        when(o1.getPollCommand()).thenReturn(wrongCommand);
        when(o2.getPollCommand()).thenReturn(wrongCommand);
        when(o3.getPollCommand()).thenReturn(wrongCommand);

        TiedPollDecider mockTiedPollDecider = new TiedPollDecider() {

            @Override
            public void executeTiePoll(PlayerPoll poll, List<PollOption> pollOptions, Runnable onTiePollFinished) {
                assertThat("PollOptions equality without order", pollOptions, containsInAnyOrder(List.of(o2,o3).toArray()));
                poll.setResultCommand(expectedCommand);
                onTiePollFinished.run();
            }
            
        };
        Poll poll = new PlayerPoll("", mockOptions, mockParticipants, 0, mockTiedPollDecider);
        poll.finish();
        assertEquals(expectedCommand, poll.getResultCommand());
    }
}
