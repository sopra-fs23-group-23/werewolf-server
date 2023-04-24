package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

public class FractionGetDTO {
    private String winner;
    private List<RoleWithPlayersGetDTO> roles;

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public List<RoleWithPlayersGetDTO> getPlayers() {
        return roles;
    }

    public void setPlayers(List<RoleWithPlayersGetDTO> roles) {
        this.roles = roles;
    }
}


// "{\"winner\":\"Werewolf or Villager\",\"players\":[{\"uid\":\"...\",\"isAlive\":true,\"roles\":[\"...\",\"...\"]},{\"uid\":\"...\",\"isAlive\":false,\"roles\":[\"...\",\"...\"]},{\"uid\":\"...\",\"isAlive\":false,\"roles\":[\"...\",\"...\"]},{\"uid\":\"...\",\"isAlive\":true,\"roles\":[\"...\",\"...\"]}]}"