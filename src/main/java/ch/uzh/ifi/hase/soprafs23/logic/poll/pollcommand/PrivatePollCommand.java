package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public abstract class PrivatePollCommand extends PollCommand{
    private final Player informationOwner;

    public PrivatePollCommand(Player affectedPlayer, Player informationOwner) {
        super(affectedPlayer);
        this.informationOwner = informationOwner;
    }

    public Player getInformationOwner() {
        return informationOwner;
    }
}
