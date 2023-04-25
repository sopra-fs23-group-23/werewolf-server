package ch.uzh.ifi.hase.soprafs23.controller;

import static ch.uzh.ifi.hase.soprafs23.service.LobbyService.LOBBYID_PATHVARIABLE;
import static ch.uzh.ifi.hase.soprafs23.service.UserService.USERAUTH_HEADER;

import java.io.IOException;

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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ch.uzh.ifi.hase.soprafs23.constant.sse.GameSseEvent;
import ch.uzh.ifi.hase.soprafs23.constant.sse.LobbySseEvent;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import ch.uzh.ifi.hase.soprafs23.service.wrapper.PlayerEmitter;

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
    public void createNewGame(@RequestHeader(USERAUTH_HEADER) String userToken, @PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId) throws IOException {
        User user = userService.getUserByToken(userToken);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateUserIsAdmin(user, lobby);
        lobbyService.validateLobbySize(lobby);
        lobbyService.closeLobby(lobby);
        lobbyService.assignRoles(lobby);
        Game game = gameService.createNewGame(lobby);
        gameService.createGameEmitter(game);
        lobbyService.sendEmitterUpdate(lobbyService.getLobbyPlayerEmitter(lobby), "", LobbySseEvent.game);
        gameService.schedule(new Runnable() {
            @Override
            public void run() {
                gameService.startGame(game);
                PlayerEmitter gameEmitter = gameService.getGameEmitter(game);
                gameService.sendGameEmitterUpdate(gameEmitter, "", GameSseEvent.start);
            }
        }, 30);
    }

    @GetMapping("/games/{lobbyId}/sse/{token}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SseEmitter getPlayerSseEmitter(@PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId, @PathVariable("token") String token) {
        User user = userService.getUserByToken(token);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateUserIsInLobby(user, lobby);
        Game game = gameService.getGame(lobby);
        return gameService.getPlayerSseEmitter(game, user);
    }

    @PutMapping("/games/{lobbyId}/votes/{optionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void vote(@RequestHeader(USERAUTH_HEADER) String token, @PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId, @PathVariable("optionId") Long optionId) {
        User user = userService.getUserByToken(token);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Game game = gameService.getGame(lobby);
        Poll poll = gameService.getCurrentPoll(game);
        gameService.validateParticipant(poll, user);
        PollParticipant participant = gameService.getParticipant(poll, user);
        PollOption option = gameService.getPollOption(poll, optionId);
        gameService.castVote(poll, participant, option);
        PlayerEmitter emitter = gameService.getGameEmitter(game);
        gameService.sendPollUpdateToAffectedUsers(emitter, poll);
    }

    @DeleteMapping("/games/{lobbyId}/votes/{optionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void removeVote(@RequestHeader(USERAUTH_HEADER) String token, @PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId, @PathVariable("optionId") Long optionId) {
        User user = userService.getUserByToken(token);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Game game = gameService.getGame(lobby);
        Poll poll = gameService.getCurrentPoll(game);
        gameService.validateParticipant(poll, user);
        PollParticipant participant = gameService.getParticipant(poll, user);
        PollOption option = gameService.getPollOption(poll, optionId);
        gameService.removeVote(poll, participant, option);
        PlayerEmitter emitter = gameService.getGameEmitter(game);
        gameService.sendPollUpdateToAffectedUsers(emitter, poll);
    }

    
}
