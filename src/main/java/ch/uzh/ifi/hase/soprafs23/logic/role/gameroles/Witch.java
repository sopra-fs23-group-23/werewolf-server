package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.KillPlayerPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.WitchSavePlayerPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.WitchKillPlayerPollCommand;
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
            
    private int remainingKillPotions = 1;
    private int remainingResurrectPotions = 1;
    
    public Witch(Supplier<List<Player>> alivePlayersGetter, Supplier<List<PollCommand>> currentStageCommands,
                 Consumer<PollCommand> removePollCommand){
        this.alivePlayersGetter = alivePlayersGetter;
        this.currentStageCommands = currentStageCommands;
        this.removePollCommand = removePollCommand;
    }

    private Player getWitch() {
        return getPlayers().stream().findFirst().get();
    }

    private List<Player> getPlayersKilledInStage() {
        return currentStageCommands.get().stream()
                .filter(KillPlayerPollCommand.class::isInstance)
                .map(c-> c.getAffectedPlayer())
                .toList();
    }

    private List<KillPlayerPollCommand> getKillPlayerPollCommands(){
        return currentStageCommands.get().stream()
                .filter(KillPlayerPollCommand.class::isInstance)
                .map(KillPlayerPollCommand.class::cast)
                .toList();
    }

    @Override
    public String getName() {
        return "Witch";
    }

    @Override
    public String getDescription() {
        return "The witch is part of the villager faction. Therefore, her goal is to save the village from the werewolves. " +
        "Thanks to her alchemical knowledge, the witch has two powerful potions which she may use once each. " +
        "The witch wakes up every night after the werewolves. When she does so, she is shown who the victim of the werewolves is. " +
        "Once in the game, she may save the victim of that night with a healing potion. " +
        "In addition, the witch has the possibility to poison a person once in the game.";
    }

    @Override
    public Optional<Poll> createNightPoll() {
        List<Player> playerKilledInStage = getPlayersKilledInStage();
        Player witch = getWitch();
        if(this.remainingResurrectPotions > 0 && (witch.isAlive() || witch.isRevivable()) &&!playerKilledInStage.isEmpty()){
            List<KillPlayerPollCommand> killedPlayerPollCommands = getKillPlayerPollCommands();
            return Optional.of(new Poll(
                    this.getClass(),
                    "Save this player from dying with your heal potion.",
                    killedPlayerPollCommands.stream().map(killPollCommand -> new PollOption(killPollCommand.getAffectedPlayer(), new WitchSavePlayerPollCommand(this.removePollCommand, killPollCommand, this::decreaseResurrectPotions, killPollCommand.getAffectedPlayer()))).toList(),
                    this.getPlayers().stream().map(p->new PollParticipant(p)).toList(),
                    15,
                    new NullResultPollDecider()));
        }
        return Optional.empty();
    }
    @Override
    public Optional<Poll> createSecondNightPoll() {
        // use kill potion
        if(this.remainingKillPotions > 0 && getWitch().isAlive()){
            List<Player> alivePlayers = alivePlayersGetter.get();
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
