package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public class KillPlayerPollCommand extends PollCommand implements StageFinishedCommand {

    public KillPlayerPollCommand(Player player) {
        super(player);
    }

    @Override
    public void execute() {
        super.execute();
        getAffectedPlayer().killPlayer();
    }

    @Override
    public void executeAfterStageFinished() {
        getAffectedPlayer().setDeadPlayerUnrevivable();
    }

    @Override
    public String toString() {
        return String.format("%s was killed.", getAffectedPlayer().getName());
    }
}
