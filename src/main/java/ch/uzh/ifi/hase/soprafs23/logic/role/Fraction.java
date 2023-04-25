package ch.uzh.ifi.hase.soprafs23.logic.role;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public interface Fraction {
    public boolean hasWon();
    public List<Player> getPlayers();
    public String getName();
}
