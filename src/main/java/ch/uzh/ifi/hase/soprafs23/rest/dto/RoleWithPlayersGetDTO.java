package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

public class RoleWithPlayersGetDTO {
    private RoleGetDTO role;

    private List<PlayerGetDTO> players;

    public void setRole(RoleGetDTO role) {
        this.role = role;
    }

    public RoleGetDTO getRole() {
        return role;
    }

    public void setPlayers(List<PlayerGetDTO> players){
        this.players = players;
    }

    public List<PlayerGetDTO> getPlayers() {
        return players;
    }
}