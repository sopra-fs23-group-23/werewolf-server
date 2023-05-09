package ch.uzh.ifi.hase.soprafs23.logic.poll;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PrivatePollCommand;

public class PrivateResultPollOption extends PollOption {

    public PrivateResultPollOption(Player player, PrivatePollCommand pollCommand) {
        super(player, pollCommand);
    }
    
}
