package ch.uzh.ifi.hase.soprafs23.logic.poll;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

public class PollOption {
    private List<PollParticipant> supporters;
    private Player player;
    private PollCommand pollCommand;

    public PollOption(Player player, PollCommand pollCommand) {
        this.player = player;
        this.pollCommand = pollCommand;
    }

    public void addSupporter(PollParticipant supporter) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addSupporter'");
    }

    public List<PollParticipant> getSupporters() {
        return supporters;
    }

    public Player getPlayer() {
        return player;
    }

    public PollCommand getPollCommand() {
        return pollCommand;
    }

    public int getSupportersAmount() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSupportersAmount'");
    }

    
}
