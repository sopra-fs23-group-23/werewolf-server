package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public class KillPlayerPollCommand implements PollCommand{
    private Player player;

    public KillPlayerPollCommand(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void execute() {
        player.killPlayer();
    }

    @Override
    public String toString() {
        return String.format("%s was killed.", player.getName());
    }
    
}
