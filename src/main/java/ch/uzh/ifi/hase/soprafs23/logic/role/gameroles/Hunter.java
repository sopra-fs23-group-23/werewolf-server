package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.PlayerObserver;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.KillPlayerPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.NullResultPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.DayVoter;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.NightVoter;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Hunter extends Role implements DayVoter, NightVoter, PlayerObserver {
    private final int voteDurationSeconds;
    private Supplier<List<Player>> alivePlayersGetter;
    private boolean died = false;
    private boolean hasShot = false;

    public Hunter(int voteDurationSeconds, Supplier<List<Player>> alivePlayersGetter) {
        this.voteDurationSeconds = voteDurationSeconds;
        this.alivePlayersGetter = alivePlayersGetter;
    }

    @Override
    public void addPlayer(Player player) {
        // ensure there is always only one hunter
        clearPlayers();
        died = false;
        player.addObserver(this);
        super.addPlayer(player);
    }

    @Override
    public Optional<Poll> createDayPoll() {
        return createHunterDiedPoll();
    }

    @Override
    public Optional<Poll> createNightPoll() {
        return createHunterDiedPoll();
    }

    private Optional<Poll> createHunterDiedPoll() {
        if (died && !hasShot) {
            List<Player> alivePlayers = alivePlayersGetter.get();
            hasShot = true;
            return Optional.of(new Poll(
                this.getClass(),
                "Who do you want to take with you to your death?",
                alivePlayers.stream().map(p->new PollOption(p, new KillPlayerPollCommand(p))).toList(),
                getPlayers().stream().map(p->new PollParticipant(p)).toList(),
                voteDurationSeconds,
                new NullResultPollDecider()
            ));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Hunter";
    }

    @Override
    public String getDescription() {
        return """
            The hunter plays in the villagers' faction.
            His goal is therefore to free the village of werewolves.
            Since the hunter always carries his bow and quiver with him, he can fire one last arrow in the event of his death.
            This enables him to choose a player who is taken to death with him.
            """;
    }

    @Override
    public void onPlayerKilled_Unrevivable(Player player) {
        died = true;
    }
}
