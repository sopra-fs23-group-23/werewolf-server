package ch.uzh.ifi.hase.soprafs23.logic.game;

import java.io.IOException;

public interface GameObserver {
    public void onNewPoll(Game game);
    public void onNewStage(Game game) throws IOException, InterruptedException;
    public void onGameFinished(Game game);
}
