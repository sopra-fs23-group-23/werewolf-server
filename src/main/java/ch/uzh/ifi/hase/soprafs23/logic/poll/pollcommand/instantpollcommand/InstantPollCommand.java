package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.instantpollcommand;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

public interface InstantPollCommand extends PollCommand{
    public void execute_instantly();

    @Override
    default void execute() {
        // At this point, pollcommand is already executed through execute_instantly()
    }
}
