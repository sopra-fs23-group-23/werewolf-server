package ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter;

import java.util.Optional;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;

public interface DayVoter extends StageVoter{
    public Optional<Poll> createDayPoll();
}
