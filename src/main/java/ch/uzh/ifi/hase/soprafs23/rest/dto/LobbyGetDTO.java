package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class LobbyGetDTO {
    private Long lobbyId;
    private Long adminUserId;
    
    public Long getLobbyId() {
        return lobbyId;
    }
    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }
    public Long getAdminUserId() {
        return adminUserId;
    }
    public void setAdminUserId(Long adminUserId) {
        this.adminUserId = adminUserId;
    }

    
}
