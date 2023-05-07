package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.Date;
import java.util.List;

public class PollGetDTO {
    private String id;
    private String role;
    private String question;
    private List<PollParticipantGetDTO> participants;
    private List<PollOptionGetDTO> pollOptions;
    private Date scheduledFinish;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public List<PollParticipantGetDTO> getParticipants() {
        return participants;
    }
    public void setParticipants(List<PollParticipantGetDTO> participants) {
        this.participants = participants;
    }
    public List<PollOptionGetDTO> getPollOptions() {
        return pollOptions;
    }
    public void setPollOptions(List<PollOptionGetDTO> pollOptions) {
        this.pollOptions = pollOptions;
    }
    public Date getScheduledFinish() {
        return scheduledFinish;
    }
    public void setScheduledFinish(Date scheduledFinish) {
        this.scheduledFinish = scheduledFinish;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    
}
