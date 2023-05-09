package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.PlayerObserver;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.KillPlayerPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
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
    }

    @Override
    public String getName() {
        return "Lover";
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "TODO";
    }

    private void killLovers() {
        List<Player> aliveLovers = getPlayers().stream().filter(Player::isAlive).toList();
        for (Player player : aliveLovers) {
            KillPlayerPollCommand killPlayerPollCommand = new KillPlayerPollCommand(player);
            killPlayerPollCommand.execute();
            pollCommandAdderConsumer.accept(killPlayerPollCommand);
        }
    }

    @Override
    public void onPlayerKilled_Unrevivable() {
        if (!killCommandExecuted) {
            killCommandExecuted = true;
            killLovers();
        }
    }
    
}
