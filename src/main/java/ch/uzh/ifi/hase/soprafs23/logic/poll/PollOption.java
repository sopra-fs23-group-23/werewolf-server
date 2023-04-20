package ch.uzh.ifi.hase.soprafs23.logic.poll;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

public class PollOption {
    private List<PollParticipant> supporters = new ArrayList<>();
    private Player player;
    private PollCommand pollCommand;

    public PollOption(Player player, PollCommand pollCommand) {
        this.player = player;
        this.pollCommand = pollCommand;
    }

    public void addSupporter(PollParticipant supporter) {
        supporters.add(supporter);
    }

    public void removeSupporter(PollParticipant supporter) {
        supporters.remove(supporter);
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
        return supporters.size();
    }

    
}
