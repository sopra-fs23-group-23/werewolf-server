package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public class KillPlayerPollCommand implements PollCommand, StageFinishedCommand{
    private Player player;

    public KillPlayerPollCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        player.killPlayer();
    }

    @Override
    public void executeAfterStageFinished() {
        player.setDeadPlayerUnrevivable();
    }

    @Override
    public String toString() {
        return String.format("%s was killed.", player.getName());
    }

    @Override
    public Player getAffectedPlayer() {
        return player;
    }
    
}
