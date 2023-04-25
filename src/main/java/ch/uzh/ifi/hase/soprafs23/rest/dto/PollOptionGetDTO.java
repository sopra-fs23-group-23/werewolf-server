package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

public class PollOptionGetDTO {
    private PlayerGetDTO player;
    private List<PlayerGetDTO> supporters;
    
    public PlayerGetDTO getPlayer() {
        return player;
    }
    public void setPlayer(PlayerGetDTO player) {
        this.player = player;
    }
    public List<PlayerGetDTO> getSupporters() {
        return supporters;
    }
    public void setSupporters(List<PlayerGetDTO> supporters) {
        this.supporters = supporters;
    }

    
}
