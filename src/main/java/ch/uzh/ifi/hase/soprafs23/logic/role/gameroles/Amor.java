package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.FirstNightVoter;

public class Amor extends Role implements FirstNightVoter {
    private BiConsumer<Player, Class<? extends Role>> addPlayerToRole;
    private Supplier<List<Player>> alivePlayersGetter;

    @Override
    public Optional<Poll> createFirstNightPoll() {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public String getName() {
        return "Amor";
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "TODO";
    }
    
}
