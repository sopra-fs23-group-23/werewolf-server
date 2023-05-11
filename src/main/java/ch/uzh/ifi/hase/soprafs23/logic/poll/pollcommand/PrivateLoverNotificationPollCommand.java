package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public class PrivateLoverNotificationPollCommand extends PrivatePollCommand {

    public PrivateLoverNotificationPollCommand(Player otherLover, Player informationOwner) {
        super(otherLover, informationOwner);
    }

    @Override
    public void execute() {
        super.execute();
        // This command serves purely as a notification
    }

    @Override
    public String toString() {
        return "You are in love with " + getAffectedPlayer().getName() + ".";
    }
    
}
