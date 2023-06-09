package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.AddPlayerToRolePollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.KillPlayerPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.FractionRole;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.DayVoter;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.FirstDayVoter;

public class Villager extends FractionRole implements DayVoter, FirstDayVoter{
    private final int voteDurationSeconds;
    private BiConsumer<Player, Class<? extends Role>> addPlayerToRole;
    private TiedPollDecider tiedPollDecider;

    public Villager(int voteDurationSeconds, BiConsumer<Player, Class<? extends Role>> addPlayerToRole,
            Supplier<List<Player>> alivePlayersGetter, TiedPollDecider tiedPollDecider) {
        super(alivePlayersGetter);
        this.addPlayerToRole = addPlayerToRole;
        this.tiedPollDecider = tiedPollDecider;
        this.voteDurationSeconds = voteDurationSeconds;
    }

    @Override
    public Optional<Poll> createDayPoll() {
        List<Player> alivePlayers = super.getAllAlivePlayers();
        return Optional.of(new Poll(
            this.getClass(),
            "Who do you suspect to be a werewolf?",
            alivePlayers.stream().map(p->new PollOption(p, new KillPlayerPollCommand(p))).toList(), 
            alivePlayers.stream().map(p->new PollParticipant(p)).toList(),
            voteDurationSeconds,
            tiedPollDecider
        ));
    }

    @Override
    public String getName() {
        return "Villager";
    }

    @Override
    public String getDescription() {
        return """
            Since a few days there are hidden werewolves among you villagers. 
            These pose a threat to the peaceful village life. Therefore, the villagers win as soon as there are no 
            werewolves left alive. Every day there is the possibility to democratically choose a person to be 
            executed. Through this execution you will be able to rid the village of werewolves.
                """;

    }

    @Override
    public Optional<Poll> createFirstDayPoll() {
        List<Player> alivePlayers = super.getAllAlivePlayers();
        return Optional.of(new Poll(
            this.getClass(),
            "Who should become the mayor?",
            alivePlayers.stream().map(p->new PollOption(p, new AddPlayerToRolePollCommand(addPlayerToRole, p, Mayor.class))).toList(), 
            alivePlayers.stream().map(p->new PollParticipant(p)).toList(),
            voteDurationSeconds,
            tiedPollDecider
        ));
    }
    
}
