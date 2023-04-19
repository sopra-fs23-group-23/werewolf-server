package ch.uzh.ifi.hase.soprafs23.logic.poll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.NullPollCommand;

public class NullPollTest {
    private NullPoll nullPoll = new NullPoll();
    @Test
    void testGetDurationSeconds() {
        assertEquals(0, nullPoll.getDurationSeconds());
    }

    @Test
    void testGetPollOptions() {
        assertTrue(nullPoll.getPollOptions().isEmpty());
    }

    @Test
    void testGetPollParticipants() {
        assertTrue(nullPoll.getPollParticipants().isEmpty());
    }

    @Test
    void testGetResultCommand() {
        assertTrue(nullPoll.getResultCommand() instanceof NullPollCommand);
    }
}
