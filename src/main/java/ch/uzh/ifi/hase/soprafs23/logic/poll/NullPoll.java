package ch.uzh.ifi.hase.soprafs23.logic.poll;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.NullPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

public class NullPoll extends Poll {

    @Override
    public void startPoll() {
        super.finish();
    }

    @Override
    public PollCommand getResultCommand() {
        return new NullPollCommand();
    }

    @Override
    public int getDurationSeconds() {
        return 0;
    }
    
}
