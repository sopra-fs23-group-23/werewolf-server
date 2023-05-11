package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.PlayerObserver;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.KillPlayerPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PrivateLoverNotificationPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.role.FractionRole;

public class Lover extends FractionRole implements PlayerObserver {
    private final Consumer<PollCommand> pollCommandAdderConsumer;
    private boolean killCommandExecuted = false;

    public Lover(Supplier<List<Player>> alivePlayersGetter, Consumer<PollCommand> pollCommandAdderConsumer) {
        super(alivePlayersGetter);
        this.pollCommandAdderConsumer = pollCommandAdderConsumer;
    }

    @Override
    public void addPlayer(Player player) {
        player.addObserver(this);
        super.addPlayer(player);
        if (getPlayers().size() >= 2) {
            notifyLoversAboutOtherLover();
        }
    }

    private void notifyLoversAboutOtherLover() {
        List<Player> lovers = getPlayers();
        for (Player lover : lovers) {
            List<Player> otherLovers = lovers.stream().filter(p -> p != lover).toList();
            otherLovers.stream().forEach(otherLover -> lover.addPrivatePollCommand(new PrivateLoverNotificationPollCommand(otherLover, lover)));
        }
    }

    @Override
    public String getName() {
        return "Lover";
    }

    @Override
    public String getDescription() {
        return " The lovers are players who have a very strong bond through the magic of the Cupid. " +
                "Their love for each other is so strong that if one of these two players dies, the other person can no longer " +
                "bear life without their great love and therefore also takes their own life. " +
                "The goal of these lovers is to still be alive at the end of the game together with their partner. "+
                "This goal is stronger than the goal originally defined by the faction. It can therefore happen that in " +
                "a relationship between a werewolf and a villager, the werewolf betrays his pack towards the end of the " +
                "game and tries to kill other werewolves with his lover during the day.";
    }

    private void killLovers() {
        List<Player> aliveLovers = getPlayers().stream().filter(Player::isAlive).toList();
        for (Player player : aliveLovers) {
            KillPlayerPollCommand killPlayerPollCommand = new KillPlayerPollCommand(player);
            killPlayerPollCommand.execute();
            killPlayerPollCommand.executeAfterStageFinished();
            pollCommandAdderConsumer.accept(killPlayerPollCommand);
        }
    }

    @Override
    public void onPlayerKilled_Unrevivable(Player player) {
        if (!killCommandExecuted) {
            killCommandExecuted = true;
            killLovers();
        }
    }
    
}
