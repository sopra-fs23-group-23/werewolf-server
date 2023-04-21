package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

public class NullPollCommand implements PollCommand{

    @Override
    public void execute() {}

    @Override
    public String toString() {
        return "";
    }
    
}
