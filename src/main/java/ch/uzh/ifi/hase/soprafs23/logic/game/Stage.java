package ch.uzh.ifi.hase.soprafs23.logic.game;

import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollObserver;

public class Stage implements PollObserver{

    // TODO replace name with enum (Night, Day)
    private String name;
    private List<StageObserver> observers;
    private Queue<Supplier<Poll>> pollSupplierQueue;
    private List<PollCommand> pollCommands;
    private Poll currentPoll;

    public Stage(String name, Queue<Supplier<Poll>> pollSupplierQueue) {
        this.name = name;
        this.pollSupplierQueue = pollSupplierQueue;
    }

    public void startStage() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startStage'");
    }

    public String getName() {
        return name;
    }

    public List<PollCommand> getPollCommands() {
        return pollCommands;
    }

    public void removePollCommand(PollCommand pollCommand) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removePollCommand'");
    }

    public void addObserver(StageObserver observer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addObserver'");
    }

    @Override
    public void onPollFinished() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onPollFinished'");
    }
    
}
