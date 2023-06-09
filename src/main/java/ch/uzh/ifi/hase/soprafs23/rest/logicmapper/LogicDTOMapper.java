package ch.uzh.ifi.hase.soprafs23.rest.logicmapper;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.game.Stage;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.role.FractionRole;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;

public final class LogicDTOMapper {
    private LogicDTOMapper(){}

    public static LobbyGetDTO convertLobbyToLobbyGetDTO(Lobby lobby) {
        LobbyGetDTO lobbyDTO = new LobbyGetDTO();
        lobbyDTO.setId(lobby.getId());
        lobbyDTO.setAdmin(convertPlayerToPlayerGetDTO(lobby.getAdmin()));
        lobbyDTO.setPlayers(
            lobby.getPlayers().stream().map(LogicDTOMapper::convertPlayerToPlayerGetDTO).toList()
        );
        lobbyDTO.setClosed(!lobby.isOpen());
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

    public static RoleGetDTO convertRoleToRoleGetDTO(Role role) {
        RoleGetDTO roleGetDTO = new RoleGetDTO();
        roleGetDTO.setRoleName(role.getName());
        roleGetDTO.setDescription(role.getDescription());
        roleGetDTO.setAmount(role.getPlayers().size());
        return roleGetDTO;
    }

    public static RoleWithPlayersGetDTO convertRoleToRoleWithPlayersGetDTO(Role role) {
        RoleWithPlayersGetDTO roleWithPlayersGetDTO = new RoleWithPlayersGetDTO();
        roleWithPlayersGetDTO.setRole(convertRoleToRoleGetDTO(role));
        roleWithPlayersGetDTO.setPlayers(
            role.getPlayers().stream().map(LogicDTOMapper::convertPlayerToPlayerGetDTO).toList()
        );
        return roleWithPlayersGetDTO;
    }

    public static StageGetDTO convertStageToStageGetDTO(Stage stage) {
        StageGetDTO stageGetDTO = new StageGetDTO();
        stageGetDTO.setType(stage.getType());
        return stageGetDTO;
    }

    /**
     * @pre PollCommand is not instance of NullPollCommand
     * @param pollCommand
     * @return
     */
    public static PollCommandGetDTO convertPollCommandToPollCommandGetDTO (PollCommand pollCommand) {
        PollCommandGetDTO pollCommandGetDTO = new PollCommandGetDTO();
        pollCommandGetDTO.setType(pollCommand.getClass().getSimpleName());
        pollCommandGetDTO.setAffectedPlayer(convertPlayerToPlayerGetDTO(pollCommand.getAffectedPlayer()));
        pollCommandGetDTO.setExecutionTime(pollCommand.getExecutionTime());
        pollCommandGetDTO.setMessage(pollCommand.toString());
        return pollCommandGetDTO;
    }

    public static GameGetDTO convertGameToGameGetDTO(Game game, List<PollCommandGetDTO> actions) {
        GameGetDTO gameGetDTO = new GameGetDTO();
        gameGetDTO.setActions(actions);
        gameGetDTO.setLobby(convertLobbyToLobbyGetDTO(game.getLobby()));
        gameGetDTO.setStage(convertStageToStageGetDTO(game.getCurrentStage()));
        gameGetDTO.setFinished(game.isFinished());
        gameGetDTO.setPollCount(game.getPollCount());
        return gameGetDTO;
    }

    public static PollOptionGetDTO convertPollOptionToPollOptionGetDTO (PollOption pollOption) {
        PollOptionGetDTO pollOptionGetDTO = new PollOptionGetDTO();
        pollOptionGetDTO.setPlayer(convertPlayerToPlayerGetDTO(pollOption.getPlayer()));
        pollOptionGetDTO.setSupporters(
            pollOption.getSupporters().stream().map(PollParticipant::getPlayer).map(LogicDTOMapper::convertPlayerToPlayerGetDTO).toList()
        );
        return pollOptionGetDTO;
    }

    public static PollParticipantGetDTO convertPollParticipantToPollParticipantGetDTO(PollParticipant pollParticipant) {
        PollParticipantGetDTO pollParticipantGetDTO = new PollParticipantGetDTO();
        pollParticipantGetDTO.setPlayer(convertPlayerToPlayerGetDTO(pollParticipant.getPlayer()));
        pollParticipantGetDTO.setRemainingVotes(pollParticipant.getRemainingVotes());
        return pollParticipantGetDTO;
    }

    public static PollGetDTO convertPollToPollGetDTO (Poll poll) {
        PollGetDTO pollGetDTO = new PollGetDTO();
        pollGetDTO.setId(poll.getId());
        pollGetDTO.setRole(poll.getRole().getSimpleName());
        pollGetDTO.setQuestion(poll.getQuestion());
        pollGetDTO.setParticipants(
            poll.getPollParticipants().stream().map(LogicDTOMapper::convertPollParticipantToPollParticipantGetDTO).toList()
        );
        pollGetDTO.setPollOptions(
            poll.getPollOptions().stream().map(LogicDTOMapper::convertPollOptionToPollOptionGetDTO).toList()
        );
        pollGetDTO.setScheduledFinish(poll.getScheduledFinish());
        return pollGetDTO;
    }

    public static FractionGetDTO convertFractionToFractionGetDTO (FractionRole fraction) {
        FractionGetDTO fractionGetDTO = new FractionGetDTO();
        fractionGetDTO.setWinner(fraction.getName());
        fractionGetDTO.setPlayers(
            fraction.getPlayers().stream().map(LogicDTOMapper::convertPlayerToPlayerGetDTO).toList()
        );
        return fractionGetDTO;
    }

    public static LobbySettingsDTO convertLobbyToLobbySettingsDTO(Lobby lobby) {
        LobbySettingsDTO lobbySettingsDTO = new LobbySettingsDTO();
        lobbySettingsDTO.setSingleVoteDurationSeconds(lobby.getSingleVoteDurationSeconds());
        lobbySettingsDTO.setPartyVoteDurationSeconds(lobby.getPartyVoteDurationSeconds());
        return lobbySettingsDTO;
    }
}
