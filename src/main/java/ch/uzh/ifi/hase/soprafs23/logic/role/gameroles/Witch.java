package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.KillPlayerPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.NullResultPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.DoubleNightVoter;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Witch extends Role implements DoubleNightVoter {
    private Supplier<List<Player>> alivePlayersGetter;
    private Supplier<List<PollCommand>> currentStageCommands;
    private Consumer<PollCommand> removePollCommand;
    private final static String name = "Witch";
    private final static String description =
            "The witch is part of the villager faction. Therefore, her goal is to save the village from the werewolves. " +
            "Thanks to her alchemical knowledge, the witch has two powerful potions which she may use once each. " +
            "The witch wakes up every night after the werewolves. When she does so, she is shown who the victim of the werewolves is. " +
            "Once in the game, she may save the victim of that night with a healing potion. " +
            "In addition, the witch has the possibility to poison a person once in the game.";
    private int remainingKillPotions;
    private int remainingResurrectPotions;
    public Witch(Supplier<List<Player>> alivePlayersGetter, Supplier<List<PollCommand>> currentStageCommands,
                 Consumer<PollCommand> removePollCommand){
        this.alivePlayersGetter = alivePlayersGetter;
        this. currentStageCommands = currentStageCommands;
        this.removePollCommand = removePollCommand;
        this.remainingKillPotions = 1;
        this.remainingResurrectPotions = 1;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Optional<Poll> createSecondNightPoll() {
        if(this.remainingResurrectPotions > 0){
            // TODO only set to 0 if action takes place
            //this.remainingResurrectPotions = 0;
            // TODO filter out player who just got killed by the werewolves
        }
        return Optional.empty();
    }

    private boolean witchKillCommandExists(List<Player> playersKilledInStage) { 
        return playersKilledInStage.contains(getPlayers().stream().findFirst().get());
    }

    private boolean isWitchAlive() {
        return getPlayers().stream().findFirst().get().isAlive();
    }

    private List<Player> getPlayersKilledInStage() {
        return currentStageCommands.get().stream()
                .filter(KillPlayerPollCommand.class::isInstance)
                .map(c->((KillPlayerPollCommand)c).getPlayer())
                .toList();
    }

    private List<Player> filterAlivePlayers() {
        List<Player> playersKilledInStage = getPlayersKilledInStage();
        return alivePlayersGetter.get().stream()
                .filter(p->!playersKilledInStage.contains(p))
                .toList();
    }

    @Override
    public Optional<Poll> createNightPoll() {
        // use kill potion
        if(this.remainingKillPotions > 0 && !witchKillCommandExists(getPlayersKilledInStage()) && isWitchAlive()){
            // TODO filter out player who already got killed to not appear in alivePlayers
            List<Player> alivePlayers = alivePlayersGetter.get();
            return Optional.of(new Poll(
                    this.getClass(),
                    "Select a player to kill with your poison potion.",
                    alivePlayers.stream().map(p->new PollOption(p, new KillPlayerPollCommand(p))).toList(),
                    // TODO: JAN wie chani filtere das nur d witch participant isch? (next line)
                    this.getPlayers().stream().filter(Player::isAlive).map(p->new PollParticipant(p)).toList(),
                    15,
                    new NullResultPollDecider()));
        }
        return Optional.empty();
    }

    // priv decreaseheal kill potion methods
}
