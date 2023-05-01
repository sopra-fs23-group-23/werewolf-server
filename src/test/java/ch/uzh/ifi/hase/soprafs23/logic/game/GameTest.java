package ch.uzh.ifi.hase.soprafs23.logic.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Villager;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Werewolf;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.DayVoter;

public class GameTest {
    // TODO: unit test rest of Game class

    private class MockPollFunction {
        private boolean called = false;
        private Role expected;

        public MockPollFunction(Role expected) {
            this.expected = expected;
        }

        public Supplier<Optional<Poll>> mockFunction(Role r) {
            assertEquals(expected, r);
            called = true;
            return ((DayVoter)r)::createDayPoll;
        }

        public boolean isCalled() {
            return called;
        }
    }

    @Test
    void testGetVotersOfType() {
        Role r1 = mock(Villager.class);
        Role r2 = mock(Werewolf.class);
        MockPollFunction mockPollFunction = new MockPollFunction(r1);

        Game.getVotersOfType(List.of(r1, r2), DayVoter.class, mockPollFunction::mockFunction);
        assertTrue(mockPollFunction.isCalled());
    }
}
