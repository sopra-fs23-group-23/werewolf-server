package ch.uzh.ifi.hase.soprafs23.logic.poll;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

public abstract class Poll {
    private List<PollObserver> observers;
    
    public void addObserver(PollObserver observer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addObserver'");
    }

    public void finish() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'finish'");
    }

    public abstract void startPoll();
    public abstract PollCommand getResultCommand();
    public abstract int getDurationSeconds();
}
