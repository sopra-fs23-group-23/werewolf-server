package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.game.StageType;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

public class StageGetDTO {
    private List<PollCommand> actions;
    private StageType type;
    
    public List<PollCommand> getActions() {
        return actions;
    }
    public void setActions(List<PollCommand> actions) {
        this.actions = actions;
    }
    public StageType getType() {
        return type;
    }
    public void setType(StageType type) {
        this.type = type;
    }

    
}
