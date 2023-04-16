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

public class Villager extends Role implements DayVoter, Fraction{
    private BiConsumer<Player, Class<? extends Role>> addPlayerToRole;
    private Supplier<List<Player>> alivePlayersGetter;
    private TiedPollDecider tiedPollDecider;

    private final static String name = "Villager";
    private final static String description = "Since a few days there are hidden werewolves among you villagers. " +
            "These pose a threat to the peaceful village life. Therefore, the villagers win as soon as there are no " +
            "werewolves left alive. Every day there is the possibility to democratically choose a person to be " +
            "executed. Through this execution you will be able to rid the village of werewolves.";

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
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
    
}
