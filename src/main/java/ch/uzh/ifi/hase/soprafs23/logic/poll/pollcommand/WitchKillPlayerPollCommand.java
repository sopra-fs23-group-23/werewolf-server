package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public class WitchKillPlayerPollCommand extends KillPlayerPollCommand{
    private Runnable decreaseKillPotion;
    public WitchKillPlayerPollCommand(Player player, Runnable decreaseKillPotion) {
        super(player);
        this.decreaseKillPotion = decreaseKillPotion;
    }
    @Override
    public void execute(){
        super.execute();
        decreaseKillPotion.run();
    }
}
