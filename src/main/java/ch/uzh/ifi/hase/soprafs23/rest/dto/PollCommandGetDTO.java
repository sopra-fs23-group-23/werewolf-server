package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class PollCommandGetDTO {
    public String type;
    public PlayerGetDTO affectedPlayer;
    public String message;
    
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public PlayerGetDTO getAffectedPlayer() {
        return affectedPlayer;
    }
    public void setAffectedPlayer(PlayerGetDTO affectedPlayer) {
        this.affectedPlayer = affectedPlayer;
    }
}
