package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
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
    public Witch(Supplier<List<Player>> alivePlayersGetter, Supplier<List<PollCommand>> currentStageCommands, Consumer<PollCommand> removePollCommand, int remainingKillPotions, int remainingResurrectPotions){
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
        return Optional.empty();
    }

    @Override
    public Optional<Poll> createNightPoll() {
        return Optional.empty();
    }
}
