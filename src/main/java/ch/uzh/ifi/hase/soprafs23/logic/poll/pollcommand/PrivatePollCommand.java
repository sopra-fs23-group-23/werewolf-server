package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public abstract interface PrivatePollCommand extends PollCommand{
    public Player getInformationOwner();
}
