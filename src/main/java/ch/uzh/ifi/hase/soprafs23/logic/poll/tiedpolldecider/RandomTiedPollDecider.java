package ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider;

import java.util.List;
import java.util.Random;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;

public class RandomTiedPollDecider implements TiedPollDecider {

    @Override
    public void executeTiePoll(Poll poll, List<PollOption> pollOptions, Runnable onTiePollFinished) {
        PollOption selected = pollOptions.get(new Random().nextInt(pollOptions.size()));
        poll.setResultCommand(selected.getPollCommand());
        onTiePollFinished.run();
    }
    
}
