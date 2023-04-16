package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.logic.game.StageType;

public class StageGetDTO {
    private StageType type;

    public StageType getType() {
        return type;
    }
    public void setType(StageType type) {
        this.type = type;
    }

    
}
