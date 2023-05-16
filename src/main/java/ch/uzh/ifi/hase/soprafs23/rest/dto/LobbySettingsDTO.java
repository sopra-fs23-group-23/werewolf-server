package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class LobbySettingsDTO {
    private Integer singleVoteDurationSeconds;
    private Integer partyVoteDurationSeconds;

    public Integer getSingleVoteDurationSeconds() {
        return singleVoteDurationSeconds;
    }
    public void setSingleVoteDurationSeconds(int singleVoteDurationSeconds) {
        this.singleVoteDurationSeconds = singleVoteDurationSeconds;
    }
    public Integer getPartyVoteDurationSeconds() {
        return partyVoteDurationSeconds;
    }
    public void setPartyVoteDurationSeconds(int partyVoteDurationSeconds) {
        this.partyVoteDurationSeconds = partyVoteDurationSeconds;
    }

    
}
