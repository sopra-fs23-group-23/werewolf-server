package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

public class GameGetDTO {
    private StageGetDTO stage;
    private LobbyGetDTO lobby;
    private List<PollCommandGetDTO> actions;

    public StageGetDTO getStage() {
        return stage;
    }
    public void setStage(StageGetDTO stage) {
        this.stage = stage;
    }
    public LobbyGetDTO getLobby() {
        return lobby;
    }
    public void setLobby(LobbyGetDTO lobby) {
        this.lobby = lobby;
    }
    public List<PollCommandGetDTO> getActions() {
        return actions;
    }
    public void setActions(List<PollCommandGetDTO> actions) {
        this.actions = actions;
    }  
}
