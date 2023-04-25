package ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.NullPollCommand;

public class NullResultPollDecider implements TiedPollDecider{

    @Override
    public void executeTiePoll(Poll poll, List<PollOption> pollOptions, Runnable onTiePollFinished) {
        poll.setResultCommand(new NullPollCommand());
        onTiePollFinished.run();
    }
    
}
