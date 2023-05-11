package ch.uzh.ifi.hase.soprafs23.logic.game;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public interface GameObserver {
    public void onNewPoll(Game game);
    public void onNewStage(Game game);
    public void onGameFinished(Game game);
    public void onPlayerDiedUnrevivable(Game game, Player player);
}
