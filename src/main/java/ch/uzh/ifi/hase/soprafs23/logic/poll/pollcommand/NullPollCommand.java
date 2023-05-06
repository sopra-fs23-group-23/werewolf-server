package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public class NullPollCommand implements PollCommand{

    @Override
    public void execute() {}

    @Override
    public String toString() {
        return "";
    }

    @Override
    public Player getAffectedPlayer() {
        return null;
    }
    
}
