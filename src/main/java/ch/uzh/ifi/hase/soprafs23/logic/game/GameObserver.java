package ch.uzh.ifi.hase.soprafs23.logic.game;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;

public interface GameObserver{
    public void onNewStage(Game game);
    public void onNewPoll(Game game, Poll poll);
}
