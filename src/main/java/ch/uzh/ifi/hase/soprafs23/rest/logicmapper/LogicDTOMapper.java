package ch.uzh.ifi.hase.soprafs23.rest.logicmapper;

import java.util.stream.StreamSupport;

import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.game.Stage;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.RoleGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.StageGetDTO;

public final class LogicDTOMapper {
    private LogicDTOMapper(){}

    public static LobbyGetDTO convertLobbyToLobbyGetDTO(Lobby lobby) {
        LobbyGetDTO lobbyDTO = new LobbyGetDTO();
        lobbyDTO.setId(lobby.getId());
        lobbyDTO.setAdmin(convertPlayerToPlayerGetDTO(lobby.getAdmin()));
        lobbyDTO.setPlayers(
            StreamSupport.stream(lobby.getPlayers().spliterator(), false).map(LogicDTOMapper::convertPlayerToPlayerGetDTO).toList()
        );
        return lobbyDTO;
    }

    public static PlayerGetDTO convertPlayerToPlayerGetDTO(Player player) {
        PlayerGetDTO playerGetDTO = new PlayerGetDTO();
        playerGetDTO.setId(player.getId());
        playerGetDTO.setName(player.getName());
        playerGetDTO.setAlive(player.isAlive());
        playerGetDTO.setAvatarUrl(player.getAvatarUrl());
        return playerGetDTO;
    }

    public static RoleGetDTO convertRoleToRoleGetDTO(Role role, int amount) {
        RoleGetDTO roleGetDTO = new RoleGetDTO();
        roleGetDTO.setRoleName(role.getName());
        roleGetDTO.setDescription(role.getDescription());
        roleGetDTO.setAmount(amount);
        return roleGetDTO;
    }

    public static StageGetDTO convertStageToStageGetDTO(Stage stage) {
        StageGetDTO stageGetDTO = new StageGetDTO();
        stageGetDTO.setType(stage.getType());
        return stageGetDTO;
    }

    public static GameGetDTO convertGameToGameGetDTO(Game game) {
        GameGetDTO gameGetDTO = new GameGetDTO();
        gameGetDTO.setActions(game.getLastStagePollCommands());
        gameGetDTO.setLobby(convertLobbyToLobbyGetDTO(game.getLobby()));
        gameGetDTO.setStage(convertStageToStageGetDTO(game.getCurrentStage()));
        return gameGetDTO;
    }
}
