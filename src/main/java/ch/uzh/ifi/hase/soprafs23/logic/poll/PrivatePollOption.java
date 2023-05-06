package ch.uzh.ifi.hase.soprafs23.logic.poll;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.instantpollcommand.PrivateInstantPollCommand;

public class PrivatePollOption extends PollOption {

    public PrivatePollOption(Player player, PrivateInstantPollCommand pollCommand) {
        super(player, pollCommand);
    }
    
}
