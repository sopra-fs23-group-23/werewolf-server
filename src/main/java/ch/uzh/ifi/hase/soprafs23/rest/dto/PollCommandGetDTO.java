package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.Date;

public class PollCommandGetDTO {
    private String type;
    private PlayerGetDTO affectedPlayer;
    private String message;
    private Date executionTime;
    
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
    public Date getExecutionTime() {
        return executionTime;
    }
    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }
}
