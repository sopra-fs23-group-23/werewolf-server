package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

public class FractionGetDTO {
    private String winner;
    private List<PlayerGetDTO> players;

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public List<PlayerGetDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerGetDTO> players) {
        this.players = players;
    }
}


// "{\"winner\":\"Werewolf or Villager\",\"players\":[{\"uid\":\"...\",\"isAlive\":true,\"roles\":[\"...\",\"...\"]},{\"uid\":\"...\",\"isAlive\":false,\"roles\":[\"...\",\"...\"]},{\"uid\":\"...\",\"isAlive\":false,\"roles\":[\"...\",\"...\"]},{\"uid\":\"...\",\"isAlive\":true,\"roles\":[\"...\",\"...\"]}]}"