package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.game.Scheduler;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.PlayerObserver;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.instantpollcommand.AddPlayerToRoleInstantPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.DayVoter;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.NightVoter;

public class Mayor extends Role implements TiedPollDecider, DayVoter, NightVoter, PlayerObserver{
    private Supplier<List<Player>> alivePlayersGetter;
    private TiedPollDecider noMayorDecider;
    private Scheduler scheduler;
    private boolean mayorDied = false;

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
    public void addPlayer(Player player) {
        // ensure there is always only one mayor
        clearPlayers();
        mayorDied = false;
        player.addObserver(this);
        super.addPlayer(player);
    }

    @Override
    public void executeTiePoll(Poll poll, List<PollOption> pollOptions, Runnable onTiePollFinished) {
        if (getPlayers().isEmpty()) {
            noMayorDecider.executeTiePoll(poll, pollOptions, onTiePollFinished);
            return;
        }
        poll.setRole(this.getClass());
        poll.setPollParticipants(getPlayers().stream().map(player -> new PollParticipant(player)).toList());
        poll.setPollOptions(pollOptions);
        poll.setTiedPollDecider(noMayorDecider);
        poll.setScheduledFinish(poll.calculateScheduledFinish(Calendar.getInstance()));
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

    private void addPlayer_BiConsumerAdapter(Player player, Class<? extends Role> roleClass) {
        addPlayer(player);
    }

    private Poll createMayorDiedPoll() {
        List<Player> alivePlayers = alivePlayersGetter.get();
        return new Poll(
            this.getClass(),
            "Who should become the mayor?",
            alivePlayers.stream().map(p->new PollOption(p, new AddPlayerToRoleInstantPollCommand(this::addPlayer_BiConsumerAdapter, p, Mayor.class))).toList(), 
            getPlayers().stream().map(p->new PollParticipant(p)).toList(),
            15,
            noMayorDecider
        );
    }

    private Optional<Poll> createMayorDiedPollIfNecessary() {
        if (mayorDied) {
            return Optional.of(createMayorDiedPoll());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Poll> createNightPoll() {
        return createMayorDiedPollIfNecessary();
    }

    @Override
    public Optional<Poll> createDayPoll() {
        return createMayorDiedPollIfNecessary();
    }

    @Override
    public void onPlayerKilled() {
        this.mayorDied = true;
    }
    
}
