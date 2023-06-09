package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.DistinctPrivateResultPoll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PrivateResultPollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PrivateAddPlayerToRolePollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.DistinctRandomTiedPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.NightVoter;

public class Cupid extends Role implements NightVoter {
    private final int voteDurationSeconds;
    private BiConsumer<Player, Class<? extends Role>> addPlayerToRole;
    private Supplier<List<Player>> alivePlayersGetter;
    private boolean firstNight = true;

    public Cupid(int voteDurationSeconds, Supplier<List<Player>> alivePlayersGetter, BiConsumer<Player, Class<? extends Role>> addPlayerToRole) {
        this.addPlayerToRole = addPlayerToRole;
        this.alivePlayersGetter = alivePlayersGetter;
        this.voteDurationSeconds = voteDurationSeconds;
    }

    @Override
    public Optional<Poll> createNightPoll() {
        if(firstNight) {
            firstNight = false;
            return Optional.of(
                new DistinctPrivateResultPoll(
                    this.getClass(),
                    "Which two players should fall in love?",
                    alivePlayersGetter.get().stream().map(player -> new PrivateResultPollOption(player, new PrivateAddPlayerToRolePollCommand(addPlayerToRole, player, Lover.class, player))).toList(),
                    getPlayers().stream().map(player -> new PollParticipant(player, 2)).findFirst().get(),
                    voteDurationSeconds,
                    new DistinctRandomTiedPollDecider())
            );
        }
        return Optional.empty();
    }

    @Override
    public String getName() {
        return "Cupid";
    }

    @Override
    public String getDescription() {
        return """
            The hunter plays in the villagers' faction.
            The cupid has the ability to choose two people on the first night who will fall in love with each other. 
            If one of these two players dies, the other person can no longer bear life without their great love and therefore also takes their own life. 
            The goal of these lovers is to still be alive at the end of the game together with their partner. Even if they are werewolf and villager.
                """;
    }
    
}
