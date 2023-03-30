package ch.uzh.ifi.hase.soprafs23.logic.poll;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

public class NullPoll extends Poll {

    @Override
    public void startPoll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startPoll'");
    }

    @Override
    public PollCommand getResultCommand() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getResultCommand'");
    }

    @Override
    public int getDurationSeconds() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDurationSeconds'");
    }
    
}
