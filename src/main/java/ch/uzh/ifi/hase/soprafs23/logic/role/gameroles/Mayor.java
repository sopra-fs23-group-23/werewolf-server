package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import java.util.List;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.game.Scheduler;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class Mayor extends Role implements TiedPollDecider{
    private Supplier<List<Player>> alivePlayersGetter;
    private TiedPollDecider noMayorDecider;
    private Scheduler scheduler;
    private final static String description = "The mayor is a role that you perform in addition to the original role. "
        + "Therefore, the role of mayor can fall into the hands of the werewolves as well as the villagers. "
        + "Your game objective is not affected by the office of mayor. The Mayor is democratically chosen at the beginning of each game. "
        + "The mayor's power is that in the event of a tie during the execution, he has the casting vote with which he may determine the person to die. "
        + "If the mayor dies, he is allowed to select a person to take over his office.";

    public Mayor(Supplier<List<Player>> alivePlayersGetter, TiedPollDecider noMayorDecider, Scheduler scheduler) {
        this.alivePlayersGetter = alivePlayersGetter;
        this.noMayorDecider = noMayorDecider;
        this.scheduler = scheduler;
    }

    @Override
    public int compareTo(Role o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }

    @Override
    public void executeTiePoll(Poll poll, List<PollOption> pollOptions, Runnable onTiePollFinished) {
        if (getPlayers().isEmpty()) {
            noMayorDecider.executeTiePoll(poll, pollOptions, onTiePollFinished);
            return;
        }
        poll.setPollParticipants(getPlayers().stream().map(player -> new PollParticipant(player)).toList());
        poll.setPollOptions(pollOptions);
        poll.setTiedPollDecider(noMayorDecider);
        scheduler.schedule(poll::finish, poll.getDurationSeconds());
    }

    @Override
    public String getName() {
        return "Mayor";
    }

    @Override
    public String getDescription() {
        return description;
    }
    
}
