package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.KillPlayerPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.WitchKillPlayerPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.instantpollcommand.RemoveCommandInstantPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.NullResultPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.DoubleNightVoter;
import javassist.NotFoundException;

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
        this.currentStageCommands = currentStageCommands;
        this.removePollCommand = removePollCommand;
        this.remainingKillPotions = 1;
        this.remainingResurrectPotions = 1;
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

    private List<KillPlayerPollCommand> getKillPlayerPollCommands(){
        return currentStageCommands.get().stream()
                .filter(KillPlayerPollCommand.class::isInstance)
                .map(KillPlayerPollCommand.class::cast)
                .toList();
    }

    private List<Player> filterAlivePlayers() {
        List<Player> playersKilledInStage = getPlayersKilledInStage();
        return alivePlayersGetter.get().stream()
                .filter(p->!playersKilledInStage.contains(p))
                .toList();
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
    public Optional<Poll> createNightPoll() {
        if(this.remainingResurrectPotions > 0 && isWitchAlive()){
            List<KillPlayerPollCommand> killedPlayerPollCommands = getKillPlayerPollCommands();
            return Optional.of(new Poll(
                    this.getClass(),
                    "Save this player from dying with your heal potion.",
                    killedPlayerPollCommands.stream().map(killPollCommand -> new PollOption(killPollCommand.getPlayer(), new RemoveCommandInstantPollCommand(this.removePollCommand, killPollCommand, this::decreaseResurrectPotions))).toList(),
                    this.getPlayers().stream().filter(Player::isAlive).map(p->new PollParticipant(p)).toList(),
                    15,
                    new NullResultPollDecider()));
        }
        return Optional.empty();
    }
    @Override
    public Optional<Poll> createSecondNightPoll() {
        // use kill potion
        if(this.remainingKillPotions > 0 && !witchKillCommandExists(getPlayersKilledInStage()) && isWitchAlive()){
            List<Player> alivePlayers = filterAlivePlayers();
            return Optional.of(new Poll(
                    this.getClass(),
                    "Select a player to kill with your poison potion.",
                    alivePlayers.stream().map(p->new PollOption(p, new WitchKillPlayerPollCommand(p, this::decreaseKillPotion))).toList(),
                    this.getPlayers().stream().filter(Player::isAlive).map(p->new PollParticipant(p)).toList(),
                    15,
                    new NullResultPollDecider()));
        }
        return Optional.empty();
    }
    private void decreaseKillPotion(){
        this.remainingKillPotions--;
    }

    private void decreaseResurrectPotions(){
        this.remainingResurrectPotions--;
    }
}
