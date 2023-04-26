package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

public class LobbyGetDTO {
    private boolean closed;
    private Long id;
    private PlayerGetDTO admin;
    private List<PlayerGetDTO> players;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public PlayerGetDTO getAdmin() {
        return admin;
    }
    public void setAdmin(PlayerGetDTO admin) {
        this.admin = admin;
    }
    public List<PlayerGetDTO> getPlayers() {
        return players;
    }
    public void setPlayers(List<PlayerGetDTO> players) {
        this.players = players;
    }
    
    public boolean isClosed() {
        return closed;
    }
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    
}
