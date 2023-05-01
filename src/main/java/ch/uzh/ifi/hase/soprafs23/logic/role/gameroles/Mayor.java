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
        // TODO Auto-generated method stub
        return "Mayor";
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "TODO";
    }
    
}
