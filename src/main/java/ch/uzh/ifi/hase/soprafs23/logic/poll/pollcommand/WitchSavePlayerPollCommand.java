package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

import java.util.function.Consumer;

public class WitchSavePlayerPollCommand extends PollCommand{
    private Consumer<PollCommand> removeCommand;
    private Runnable decreaseHealPotion;
    private PollCommand pollCommand;

    public WitchSavePlayerPollCommand(Consumer<PollCommand> removeCommand, PollCommand pollCommand, Runnable decreaseHealPotion, Player player){
        super(player);
        this.removeCommand = removeCommand;
        this.pollCommand = pollCommand;
        this.decreaseHealPotion = decreaseHealPotion;
    }

    @Override
    public void execute() {
        super.execute();
        removeCommand.accept(pollCommand);
        decreaseHealPotion.run();
        getAffectedPlayer().revivePlayer();
    }

    @Override
    public String toString() {
        return String.format("%s was saved from dying by the witch.", getAffectedPlayer().getName());
    }
}
