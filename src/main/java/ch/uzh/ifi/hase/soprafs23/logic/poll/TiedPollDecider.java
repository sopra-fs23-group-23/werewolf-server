package ch.uzh.ifi.hase.soprafs23.logic.poll;

import java.util.List;

public interface TiedPollDecider {
    public void executeTiePoll(List<PollOption> pollOptions, PollObserver observer);
}
