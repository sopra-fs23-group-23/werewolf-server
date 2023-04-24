package ch.uzh.ifi.hase.soprafs23.logic.game;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.role.Fraction;

public interface GameObserver{
    public void onNewStage(Game game);
    public void onNewPoll(Game game, Poll poll);
    public void onGameEnd(Game game, Fraction fraction);
}
