package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public interface PollCommand {
    public void execute();
    public Player getAffectedPlayer();
}
