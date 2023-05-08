package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import java.util.List;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.PlayerObserver;
import ch.uzh.ifi.hase.soprafs23.logic.role.Fraction;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class Lover extends Role implements Fraction, PlayerObserver {
    private final Supplier<List<Player>> alivePlayersGetter;
    private boolean killCommandExecuted = false;

    public Lover(Supplier<List<Player>> alivePlayersGetter) {
        this.alivePlayersGetter = alivePlayersGetter;
    }

    @Override
    public void addPlayer(Player player) {
        player.addObserver(this);
        super.addPlayer(player);
    }

    @Override
    public boolean hasWon() {
        for(Player player : alivePlayersGetter.get()) {
            if(!getPlayers().contains(player)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return "Lover";
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "TODO";
    }

    private void killLovers() {
        getPlayers().stream().forEach(Player::killPlayer);
    }

    @Override
    public void onPlayerKilled() {
        if (!killCommandExecuted) {
            killCommandExecuted = true;
            killLovers();
        }
    }
    
}
