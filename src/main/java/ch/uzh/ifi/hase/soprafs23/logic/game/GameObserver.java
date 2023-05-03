package ch.uzh.ifi.hase.soprafs23.logic.game;

public interface GameObserver {
    public void onNewPoll(Game game);
    public void onNewStage(Game game);
    public void onGameFinished(Game game);
}
