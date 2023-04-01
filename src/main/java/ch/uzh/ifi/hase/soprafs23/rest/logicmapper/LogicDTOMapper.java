package ch.uzh.ifi.hase.soprafs23.rest.logicmapper;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;

public final class LogicDTOMapper {
    private LogicDTOMapper(){}

    public static LobbyGetDTO convertLobbyToLobbyGetDTO(Lobby lobby) {
        LobbyGetDTO dto = new LobbyGetDTO();
        dto.setLobbyId(lobby.getId());
        dto.setAdminUserId(lobby.getAdmin().getId());
        return dto;
    }
}
