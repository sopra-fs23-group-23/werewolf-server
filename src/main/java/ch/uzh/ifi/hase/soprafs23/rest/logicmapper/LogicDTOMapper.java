package ch.uzh.ifi.hase.soprafs23.rest.logicmapper;

import java.util.Calendar;
import java.util.stream.StreamSupport;

import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.game.Stage;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PollCommandGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PollGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PollOptionGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PollParticipantGetDTO;
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

    public static PollCommandGetDTO convertPollCommandToPollCommandGetDTO (PollCommand pollCommand) {
        PollCommandGetDTO pollCommandGetDTO = new PollCommandGetDTO();
        pollCommandGetDTO.setType(pollCommand.getClass().getSimpleName());
        pollCommandGetDTO.setMessage(pollCommand.toString());
        return pollCommandGetDTO;
    }

    public static GameGetDTO convertGameToGameGetDTO(Game game) {
        GameGetDTO gameGetDTO = new GameGetDTO();
        gameGetDTO.setActions(game.getLastStagePollCommands().stream().map(LogicDTOMapper::convertPollCommandToPollCommandGetDTO).toList());
        gameGetDTO.setLobby(convertLobbyToLobbyGetDTO(game.getLobby()));
        gameGetDTO.setStage(convertStageToStageGetDTO(game.getCurrentStage()));
        return gameGetDTO;
    }

    public static PollOptionGetDTO convertPollOptionToPollOptionGetDTO (PollOption pollOption) {
        PollOptionGetDTO pollOptionGetDTO = new PollOptionGetDTO();
        pollOptionGetDTO.setPlayer(convertPlayerToPlayerGetDTO(pollOption.getPlayer()));
        pollOptionGetDTO.setSupporters(
            StreamSupport.stream(pollOption.getSupporters().spliterator(), false).map(PollParticipant::getPlayer).map(LogicDTOMapper::convertPlayerToPlayerGetDTO).toList()
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
        pollGetDTO.setRole(poll.getRole().getSimpleName());
        pollGetDTO.setQuestion(poll.getQuestion());
        pollGetDTO.setParticipants(
            StreamSupport.stream(poll.getPollParticipants().spliterator(), false).map(LogicDTOMapper::convertPollParticipantToPollParticipantGetDTO).toList()
        );
        pollGetDTO.setPollOptions(
            StreamSupport.stream(poll.getPollOptions().spliterator(), false).map(LogicDTOMapper::convertPollOptionToPollOptionGetDTO).toList()
        );
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, poll.getDurationSeconds());
        pollGetDTO.setScheduledFinish(calendar.getTime());
        return pollGetDTO;
    }
}
