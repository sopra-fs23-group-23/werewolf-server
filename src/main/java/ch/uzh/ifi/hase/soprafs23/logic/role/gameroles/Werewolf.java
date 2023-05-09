package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.KillPlayerPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.NullResultPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.FractionRole;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.NightVoter;

public class Werewolf extends FractionRole implements NightVoter {
    private final static String name = "Werewolf";
    private final static String description = "The werewolves take on the role of the antagonists in this game. " +
            "They win as soon as no villagers are left alive. Every night the werewolves wake up and vote on " +
            "their next victim. The person with the most votes dies. However, the werewolves have to be careful," +
            " as no one dies if they can’t agree.";

    public Werewolf(Supplier<List<Player>> alivePlayersGetter) {
        super(alivePlayersGetter);
    }

    @Override
    public Optional<Poll> createNightPoll() {
        List<Player> alivePlayers = super.getAllAlivePlayers();
        return Optional.of(new Poll(
            this.getClass(),
            "Who do you want to kill tonight?",
            alivePlayers.stream().map(p->new PollOption(p, new KillPlayerPollCommand(p))).toList(),
            getPlayers().stream().filter(Player::isAlive).map(p->new PollParticipant(p)).toList(),
            15,
            new NullResultPollDecider()));
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
