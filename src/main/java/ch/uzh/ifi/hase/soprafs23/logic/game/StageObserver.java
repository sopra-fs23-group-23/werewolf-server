package ch.uzh.ifi.hase.soprafs23.logic.game;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;

public interface StageObserver {
    public void onStageFinished();
    public void onNewPoll(Poll poll);
}
