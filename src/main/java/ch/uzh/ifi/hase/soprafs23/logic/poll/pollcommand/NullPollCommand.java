package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

public class NullPollCommand extends PollCommand{

    public NullPollCommand() {
        super(null);
    }

    @Override
    public void execute() {}

    @Override
    public String toString() {
        return "";
    }
    
}
