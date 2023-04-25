package ch.uzh.ifi.hase.soprafs23.logic.poll;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public class PollParticipant {
    private int remainingVotes;
    private Player player;

    public PollParticipant(Player player) {
        this.player = player;
        this.remainingVotes = 1;
    }

    public PollParticipant(int remainingVotes, Player player) {
        this.remainingVotes = remainingVotes;
        this.player = player;
    }

    public int getRemainingVotes() {
        return remainingVotes;
    }

    public void increaseRemainingVotes() {
        this.remainingVotes++;
    }

    public void decreaseRemainingVotes() {
        this.remainingVotes--;
    }

    public Player getPlayer() {
        return player;
    }
}
