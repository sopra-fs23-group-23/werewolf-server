package ch.uzh.ifi.hase.soprafs23.logic.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Mayor;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Villager;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Werewolf;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.DayVoter;

public class GameIntegrationTest {
    // TODO: unit test rest of Game class

    private class MockPollFunction {
        private boolean called = false;
        private List<Role> callers = new ArrayList<>();

        public Supplier<Optional<Poll>> mockFunction(Role r) {
            callers.add(r);
            called = true;
            return ((DayVoter)r)::createDayPoll;
        }

        public boolean isCalled() {
            return called;
        }

        public List<Role> getCallers() {
            return callers;
        }
    }

    @Test
    void testGetVotersOfType() {
        Role r1 = new Villager(null, null, null);
        Role r2 = new Werewolf(null);
        Role r3 = new Mayor(null, null, null);

        MockPollFunction mockPollFunction = new MockPollFunction();

        Game.getVotersOfType(List.of(r1, r2, r3), DayVoter.class, mockPollFunction::mockFunction);
        assertTrue(mockPollFunction.isCalled());
        assertEquals(List.of(r3, r1), mockPollFunction.getCallers());
    }
}
