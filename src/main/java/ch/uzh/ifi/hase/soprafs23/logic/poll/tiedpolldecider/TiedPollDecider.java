package ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;

public interface TiedPollDecider {
    public void executeTiePoll(Poll poll, List<PollOption> pollOptions, Runnable onTiePollFinished);
}
