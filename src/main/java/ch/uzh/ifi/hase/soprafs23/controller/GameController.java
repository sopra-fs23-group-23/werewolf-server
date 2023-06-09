package ch.uzh.ifi.hase.soprafs23.controller;

import static ch.uzh.ifi.hase.soprafs23.service.LobbyService.LOBBYID_PATHVARIABLE;
import static ch.uzh.ifi.hase.soprafs23.service.UserService.USERAUTH_HEADER;

import ch.uzh.ifi.hase.soprafs23.rest.dto.FractionGetDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.game.Scheduler;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PollGetDTO;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;

@RestController
public class GameController {
    private final UserService userService;
    private final LobbyService lobbyService;
    private final GameService gameService;

    public GameController(UserService userService, LobbyService lobbyService, GameService gameService) {
        this.userService = userService;
        this.lobbyService = lobbyService;
        this.gameService = gameService;
    }

    @PostMapping("/games/{lobbyId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createNewGame(@RequestHeader(USERAUTH_HEADER) String userToken, @PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId) {
        User user = userService.getUserByToken(userToken);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateUserIsAdmin(user, lobby);
        lobbyService.validateLobbySize(lobby);
        lobbyService.closeLobby(lobby);
        lobbyService.reInstatiatePlayers(lobby);
        Game game = gameService.createNewGame(lobby);
        lobbyService.instantiateRoles(lobby, game);
        lobbyService.assignRoles(lobby);
        Scheduler.getInstance().schedule(() -> gameService.startGame(game), 10);
    }

    @GetMapping("/games/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGame(@RequestHeader(USERAUTH_HEADER) String token, @PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId) {
        User user = userService.getUserByToken(token);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateUserIsInLobby(user, lobby);
        Player player = lobbyService.getPlayerOfUser(user, lobby);
        Game game = gameService.getGame(lobby);
        gameService.validateGameStarted(game);
        GameGetDTO gameGetDTO = gameService.toGameGetDTO(game);
        return gameService.mergePlayerPollCommandsToGameGetDTO(gameGetDTO, player);
    }

    @GetMapping("/games/{lobbyId}/polls")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PollGetDTO getPoll(@RequestHeader(USERAUTH_HEADER) String token, @PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId) {
        User user = userService.getUserByToken(token);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateUserIsInLobby(user, lobby);
        Game game = gameService.getGame(lobby);
        gameService.validateGameStarted(game);
        Poll poll = gameService.getCurrentPoll(game);
        PollGetDTO pollGetDTO = gameService.toPollGetDTO(poll);
        if (gameService.isPollParticipant(poll, user)) {
            return pollGetDTO;
        } else {
            return gameService.censorPollGetDTO(pollGetDTO);
        }
    }

    @GetMapping("/games/{lobbyId}/winner")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public FractionGetDTO getWinner(@RequestHeader(USERAUTH_HEADER) String token, @PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId) {
        User user = userService.getUserByToken(token);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Game game = gameService.getGame(lobby);
        lobbyService.validateUserIsInLobby(user, lobby);
        gameService.validateGameFinished(game);
        return gameService.getFractionGetDTO(game);
    }

    @PutMapping("/games/{lobbyId}/votes/{optionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void vote(@RequestHeader(USERAUTH_HEADER) String token, @PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId, @PathVariable("optionId") Long optionId) {
        User user = userService.getUserByToken(token);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateUserIsInLobby(user, lobby);
        Game game = gameService.getGame(lobby);
        gameService.validateGameStarted(game);
        Poll poll = gameService.getCurrentPoll(game);
        gameService.validateParticipant(poll, user);
        PollParticipant participant = gameService.getParticipant(poll, user);
        PollOption option = gameService.getPollOption(poll, optionId);
        gameService.castVote(poll, participant, option);
    }

    @DeleteMapping("/games/{lobbyId}/votes/{optionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void removeVote(@RequestHeader(USERAUTH_HEADER) String token, @PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId, @PathVariable("optionId") Long optionId) {
        User user = userService.getUserByToken(token);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateUserIsInLobby(user, lobby);
        Game game = gameService.getGame(lobby);
        gameService.validateGameStarted(game);
        Poll poll = gameService.getCurrentPoll(game);
        gameService.validateParticipant(poll, user);
        PollParticipant participant = gameService.getParticipant(poll, user);
        PollOption option = gameService.getPollOption(poll, optionId);
        gameService.removeVote(poll, participant, option);
    }

    
}
