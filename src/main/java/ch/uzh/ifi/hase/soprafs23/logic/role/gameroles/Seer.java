package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PrivateRevealRolesNotificationPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.NullResultPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.NightVoter;

public class Seer extends Role implements NightVoter {
    private final Supplier<List<Player>> alivePlayersGetter;
    private final Function<Player, Collection<Role>> rolesPerPlayer;

    public Seer(Supplier<List<Player>> alivePlayersGetter, Function<Player, Collection<Role>> rolesPerPlayer) {
        this.alivePlayersGetter = alivePlayersGetter;
        this.rolesPerPlayer = rolesPerPlayer;
    }

    @Override
    public Optional<Poll> createNightPoll() {
        Player seerPlayer = getPlayers().get(0);
         if (seerPlayer.isAlive()) {
                List<Player> alivePlayers = alivePlayersGetter.get();
                return Optional.of(new Poll(
                    this.getClass(),
                    "Whose roles do you want to reveal?",
                    alivePlayers.stream().map(player->new PollOption(player, new PrivateRevealRolesNotificationPollCommand(player, seerPlayer, rolesPerPlayer))).toList(),
                    this.getPlayers().stream().map(p->new PollParticipant(p)).toList(),
                    15,
                    new NullResultPollDecider()
                ));
         }
         return Optional.empty();
    }

    @Override
    public String getName() {
        return "Seer";
    }

    @Override
    public String getDescription() {
        return """
            The Seer plays together with the villagers and thus tries to stop the werewolf invasion. 
            Thanks to their magical abilities, seers can choose one person every night to find out whether they are good or evil.
                """;
    }
    
}
