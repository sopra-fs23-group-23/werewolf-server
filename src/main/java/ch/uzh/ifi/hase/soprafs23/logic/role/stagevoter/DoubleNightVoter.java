package ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;

public interface DoubleNightVoter extends NightVoter{
    public Poll createSecondNightPoll();
}
