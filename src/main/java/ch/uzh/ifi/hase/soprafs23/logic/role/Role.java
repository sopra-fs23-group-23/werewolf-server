package ch.uzh.ifi.hase.soprafs23.logic.role;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public abstract class Role implements Comparable<Role>{
    private List<Player> players = new ArrayList<>();

    public abstract String getName();
    public abstract String getDescription();

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }
}
