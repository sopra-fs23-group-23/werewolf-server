package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

import java.util.List;

public class RoleWithPlayersGetDTO extends RoleGetDTO{

    private List<Player> players;

    public void setPlayers(Role role){
        this.players = role.getPlayers();
    }

    public List<Player> getPlayers() {
        return players;
    }
}