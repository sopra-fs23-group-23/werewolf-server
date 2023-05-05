package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.DoubleNightVoter;

import java.util.Optional;

public class Witch extends Role implements DoubleNightVoter {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Optional<Poll> createSecondNightPoll() {
        return Optional.empty();
    }

    @Override
    public Optional<Poll> createNightPoll() {
        return Optional.empty();
    }
}
