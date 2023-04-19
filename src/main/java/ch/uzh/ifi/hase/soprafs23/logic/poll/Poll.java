package ch.uzh.ifi.hase.soprafs23.logic.poll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

public abstract class Poll {
    private List<PollObserver> observers = new ArrayList<>();
    
    public void addObserver(PollObserver observer) {
        observers.add(observer);
    }

    public void finish() {
        observers.stream().forEach(o->o.onPollFinished());
    }

    public abstract PollCommand getResultCommand();
    public abstract int getDurationSeconds();
    public abstract Collection<PollParticipant> getPollParticipants();
    public abstract Collection<PollOption> getPollOptions();
    public abstract String getQuestion();
}
