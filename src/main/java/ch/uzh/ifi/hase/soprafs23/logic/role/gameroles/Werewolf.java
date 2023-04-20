package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import java.util.List;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.KillPlayerPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.NullResultPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Fraction;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.NightVoter;

public class Werewolf extends Role implements NightVoter, Fraction{
    private Supplier<List<Player>> alivePlayersGetter;
    private final static String name = "Werewolf";
    private final static String description = "The werewolves take on the role of the antagonists in this game. " +
            "They win as soon as no villagers are left alive. Every night the werewolves wake up and vote on " +
            "their next victim. The person with the most votes dies. However, the werewolves have to be careful," +
            " as no one dies if they can’t agree.";

    public Werewolf(Supplier<List<Player>> alivePlayersGetter) {
        this.alivePlayersGetter = alivePlayersGetter;
    }

    @Override
    public int compareTo(Role arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean hasWon() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasWon'");
    }

    @Override
    public Poll createNightPoll() {
        List<Player> alivePlayers = alivePlayersGetter.get();
        return new Poll(
            "Who do you want to kill tonight?",
            alivePlayers.stream().map(p->new PollOption(p, new KillPlayerPollCommand(p))).toList(),
            getPlayers().stream().map(p->new PollParticipant(p)).toList(),
            30,
            new NullResultPollDecider());
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
