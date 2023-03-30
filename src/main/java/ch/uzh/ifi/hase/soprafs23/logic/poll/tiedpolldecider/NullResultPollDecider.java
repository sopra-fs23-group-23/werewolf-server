package ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.poll.PollObserver;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;

public class NullResultPollDecider implements TiedPollDecider{

    @Override
    public void executeTiePoll(List<PollOption> pollOptions, PollObserver observer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'executeTiePoll'");
    }
    
}
