package ch.uzh.ifi.hase.soprafs23.logic.role;

import java.util.List;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public abstract class FractionRole extends Role{
    private final Supplier<List<Player>> alivePlayersGetter;

    public FractionRole(Supplier<List<Player>> alivePlayersGetter) {
        this.alivePlayersGetter = alivePlayersGetter;
    }
    
    public boolean hasWon() {
        for(Player player : getAllAlivePlayers()) {
            if(!getPlayers().contains(player)) {
                return false;
            }
        }
        return true;
    }

    protected List<Player> getAllAlivePlayers() {
        return alivePlayersGetter.get();
    }
}
