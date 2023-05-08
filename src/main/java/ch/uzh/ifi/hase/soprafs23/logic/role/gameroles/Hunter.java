package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.DayVoter;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class Hunter extends Role implements DayVoter {
    private BiConsumer<Player, Class<? extends Role>> addPlayerToRole;
    private Supplier<List<Player>> alivePlayersGetter;

    public Hunter(Supplier<List<Player>> alivePlayersGetter, BiConsumer<Player, Class<? extends Role>> addPlayerToRole) {
        this.addPlayerToRole = addPlayerToRole;
        this.alivePlayersGetter = alivePlayersGetter;
    }

    @Override
    public Optional<Poll> createDayPoll() {
        return Optional.of(null);
    }

    @Override
    public String getName() {
        return "Hunter";
    }

    @Override
    public String getDescription() {
        return """
                The hunter plays in the villagers' faction. His goal is therefore to free the village of werewolves.
                Since the hunter carries his bow and quiver with him everywhere, he can fire one last arrow in the event of his death.
                This enables him to choose a person who is still alive and who will die as well.
                """;
    }
}
