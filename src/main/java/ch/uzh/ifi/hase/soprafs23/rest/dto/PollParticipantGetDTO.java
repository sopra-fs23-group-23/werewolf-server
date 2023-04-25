package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class PollParticipantGetDTO {
    private PlayerGetDTO player;
    private int remainingVotes;
    
    public PlayerGetDTO getPlayer() {
        return player;
    }
    public void setPlayer(PlayerGetDTO player) {
        this.player = player;
    }
    public int getRemainingVotes() {
        return remainingVotes;
    }
    public void setRemainingVotes(int remainingVotes) {
        this.remainingVotes = remainingVotes;
    }

    
}
