package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.DistinctPrivateResultPoll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PrivateResultPollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PrivateAddPlayerToRolePollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.DistinctRandomTiedPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.FirstNightVoter;

public class Cupid extends Role implements FirstNightVoter {
    private BiConsumer<Player, Class<? extends Role>> addPlayerToRole;
    private Supplier<List<Player>> alivePlayersGetter;

    public Cupid(Supplier<List<Player>> alivePlayersGetter, BiConsumer<Player, Class<? extends Role>> addPlayerToRole) {
        this.addPlayerToRole = addPlayerToRole;
        this.alivePlayersGetter = alivePlayersGetter;
    }

    @Override
    public Optional<Poll> createFirstNightPoll() {
        return Optional.of(
            new DistinctPrivateResultPoll(
                this.getClass(),
                "Which two players should fall in love?",
                alivePlayersGetter.get().stream().map(player -> new PrivateResultPollOption(player, new PrivateAddPlayerToRolePollCommand(addPlayerToRole, player, Lover.class, player))).toList(),
                getPlayers().stream().map(player -> new PollParticipant(player, 2)).findFirst().get(),
                15,
                new DistinctRandomTiedPollDecider())
        );
    }

    @Override
    public String getName() {
        return "Cupid";
    }

    @Override
    public String getDescription() {
        return """
            As the cupid you play in the villagers' faction. Your goal is therefore to exterminate the werewolves. 
            The cupid has the ability to choose two people on the first night who will fall in love with each other. 
            If one of these two players dies, the other person can no longer bear life without their great love and therefore also takes their own life. 
            The goal of these lovers is to still be alive at the end of the game together with their partner. 
            This goal is stronger than the goal originally defined by the faction. It can therefore happen that in a relationship between a werewolf and a villager, 
            the werewolf betrays his pack towards the end of the game and tries to kill other werewolves with his lover during the day. 
                """;
    }
    
}
