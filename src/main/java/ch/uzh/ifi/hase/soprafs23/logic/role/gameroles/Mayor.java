package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import java.util.List;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.PlayerObserver;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollObserver;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.DayVoter;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.NightVoter;

public class Mayor extends Role implements NightVoter, DayVoter, PlayerObserver, TiedPollDecider{
    private Supplier<List<Player>> alivePlayersGetter;
    private boolean mayorWasKilled = false;

    public Mayor(Supplier<List<Player>> alivePlayersGetter) {
        this.alivePlayersGetter = alivePlayersGetter;
    }

    @Override
    public int compareTo(Role arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }

    @Override
    public void executeTiePoll(List<PollOption> pollOptions, PollObserver observer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'executeTiePoll'");
    }

    @Override
    public void onPlayerKilled() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onPlayerKilled'");
    }

    @Override
    public Poll createDayPoll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createDayPoll'");
    }

    @Override
    public Poll createNightPoll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createNightPoll'");
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDescription'");
    }
    
}
