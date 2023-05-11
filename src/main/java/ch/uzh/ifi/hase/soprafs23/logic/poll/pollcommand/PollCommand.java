package ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand;

import java.util.Date;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public abstract class PollCommand {
    private final Player affectedPlayer;
    private Date executionTime;

    public PollCommand(Player affectedPlayer) {
        this.affectedPlayer = affectedPlayer;
        this.executionTime = new Date();
    }

    public void execute() {
        executionTime = new Date();
    }

    public Player getAffectedPlayer() {
        return affectedPlayer;
    }

    public Date getExecutionTime() {
        return executionTime;
    }
}
