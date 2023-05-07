package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.instantpollcommand;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

import java.util.function.Consumer;

public class RemoveCommandInstantPollCommand implements InstantPollCommand{
    private Consumer<PollCommand> removeCommand;
    private Runnable decreaseHealPotion;
    private PollCommand pollCommand;

    public RemoveCommandInstantPollCommand(Consumer<PollCommand> removeCommand, PollCommand pollCommand, Runnable decreaseHealPotion){
        this.removeCommand = removeCommand;
        this.pollCommand = pollCommand;
        this.decreaseHealPotion = decreaseHealPotion;
    }

    @Override
    public void execute_instantly() {
        removeCommand.accept(pollCommand);
        decreaseHealPotion.run();
    }
}
