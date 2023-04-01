package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Fraction;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.DayVoter;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.FirstDayVoter;

public class Villager extends Role implements FirstDayVoter, DayVoter, Fraction{
    private BiConsumer<Player, Class<? extends Role>> addPlayerToRole;
    private Supplier<List<Player>> alivePlayersGetter;
    private TiedPollDecider tiedPollDecider;

    public Villager(BiConsumer<Player, Class<? extends Role>> addPlayerToRole,
            Supplier<List<Player>> alivePlayersGetter, TiedPollDecider tiedPollDecider) {
        this.addPlayerToRole = addPlayerToRole;
        this.alivePlayersGetter = alivePlayersGetter;
        this.tiedPollDecider = tiedPollDecider;
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
    public Poll createDayPoll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createDayPoll'");
    }

    @Override
    public Poll createFirstDayPoll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createFirstDayPoll'");
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
