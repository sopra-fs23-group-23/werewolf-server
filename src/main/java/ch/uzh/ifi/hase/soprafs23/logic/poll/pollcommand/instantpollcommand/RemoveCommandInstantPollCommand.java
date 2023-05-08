package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.instantpollcommand;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

import java.util.function.Consumer;

public class RemoveCommandInstantPollCommand implements InstantPollCommand{
    private Consumer<PollCommand> removeCommand;
    private Runnable decreaseHealPotion;
    private PollCommand pollCommand;
    private Player player;

    public RemoveCommandInstantPollCommand(Consumer<PollCommand> removeCommand, PollCommand pollCommand, Runnable decreaseHealPotion, Player player){
        this.removeCommand = removeCommand;
        this.pollCommand = pollCommand;
        this.decreaseHealPotion = decreaseHealPotion;
        this.player = player;
    }

    @Override
    public void execute_instantly() {
        removeCommand.accept(pollCommand);
        decreaseHealPotion.run();
    }

    @Override
    public String toString() {
        return String.format("%s was saved from dying by the witch.", player.getName());
    }

    @Override
    public Player getAffectedPlayer() {
        return player;
    }
}
