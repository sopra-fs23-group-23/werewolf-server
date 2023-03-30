package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import java.util.List;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.role.Fraction;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.NightVoter;

public class Werewolf extends Role implements NightVoter, Fraction{
    private Supplier<List<Player>> alivePlayersGetter;

    public Werewolf(Supplier<List<Player>> alivePlayersGetter) {
        this.alivePlayersGetter = alivePlayersGetter;
    }

    @Override
    public int compareTo(Role arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }

    @Override
    public boolean hasWon() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasWon'");
    }

    @Override
    public Poll createNightPoll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createNightPoll'");
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDescription'");
    }
    
}
