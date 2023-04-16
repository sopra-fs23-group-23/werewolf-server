package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

public class GameGetDTO {
    private StageGetDTO stage;
    private LobbyGetDTO lobby;
    private List<PollCommand> actions;

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
    public List<PollCommand> getActions() {
        return actions;
    }
    public void setActions(List<PollCommand> actions) {
        this.actions = actions;
    }    
}
