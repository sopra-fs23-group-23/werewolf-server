package ch.uzh.ifi.hase.soprafs23.logic.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollObserver;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

public class Stage implements PollObserver{
    private StageType type;
    private List<StageObserver> observers = new ArrayList<>();
    private Queue<Supplier<Poll>> pollSupplierQueue;
    private List<PollCommand> pollCommands = new ArrayList<>();
    private Poll currentPoll;

    public Stage(StageType type, Queue<Supplier<Poll>> pollSupplierQueue) {
        this.type = type;
        this.pollSupplierQueue = pollSupplierQueue;
    }

    private void startNextPoll() {
        if(pollSupplierQueue.isEmpty()) {
            finishStage();
            return;
        }
        currentPoll = pollSupplierQueue.poll().get();
        currentPoll.addObserver(this);
        notifyObserversAboutNewPoll();
    }

    private void finishStage() {
        observers.stream().forEach(s -> s.onStageFinished());
    }

    private void notifyObserversAboutNewPoll() {
        observers.stream().forEach(s -> s.onNewPoll(currentPoll));
    }

    public void startStage() {
        startNextPoll();
    }

    public StageType getType() {
        return type;
    }

    public List<PollCommand> getPollCommands() {
        return pollCommands;
    }

    public void removePollCommand(PollCommand pollCommand) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removePollCommand'");
    }

    public void addObserver(StageObserver observer) {
        observers.add(observer);
    }

    @Override
    public void onPollFinished() {
        pollCommands.add(currentPoll.getResultCommand());
        startNextPoll();
    }
    
}
