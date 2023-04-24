package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

import java.util.List;

public class RoleWithPlayersGetDTO extends RoleGetDTO{

    private List<PlayerGetDTO> players;

    public void setPlayers(List<PlayerGetDTO> players){
        this.players = players;
    }

    public List<PlayerGetDTO> getPlayers() {
        return players;
    }
}