package ch.uzh.ifi.hase.soprafs23.logic.poll;

import java.util.Collection;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.NullPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

public class NullPoll extends Poll {

    @Override
    public PollCommand getResultCommand() {
        return new NullPollCommand();
    }

    @Override
    public int getDurationSeconds() {
        return 0;
    }

    @Override
    public Collection<PollParticipant> getPollParticipants() {
        return List.of();
    }

    @Override
    public Collection<PollOption> getPollOptions() {
        return List.of();
    }

    @Override
    public String getQuestion() {
        return "";
    }

    @Override
    public void castVote(PollParticipant voter, PollOption pollOption) throws IllegalArgumentException{
        throw new IllegalArgumentException("Unable to vote in NullPoll.");
    }
    
}
