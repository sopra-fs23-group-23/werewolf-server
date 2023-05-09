package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public class PrivateLoverNotificationPollCommand implements PrivatePollCommand {
    private final Player otherLover;
    private final Player informationOwner;

    public PrivateLoverNotificationPollCommand(Player otherLover, Player informationOwner) {
        this.otherLover = otherLover;
        this.informationOwner = informationOwner;
    }

    @Override
    public void execute() {
        // This command serves purely as a notification
    }

    @Override
    public Player getAffectedPlayer() {
        return otherLover;
    }

    @Override
    public Player getInformationOwner() {
        return informationOwner;
    }

    @Override
    public String toString() {
        return "You are in love with " + otherLover.getName() + ".";
    }
    
}
