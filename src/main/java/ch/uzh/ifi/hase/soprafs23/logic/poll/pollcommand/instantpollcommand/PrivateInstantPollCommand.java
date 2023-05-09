package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.instantpollcommand;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public abstract interface PrivateInstantPollCommand extends InstantPollCommand{
    public Player getInformationOwner();
}
