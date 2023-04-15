package ch.uzh.ifi.hase.soprafs23.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs23.constant.sse.LobbySseEvent;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;

@RestController
public class GameController {
    private final UserService userService;
    private final LobbyService lobbyService;
    private final GameService gameService;

    // TODO move to respective services
    public static final String USERAUTH_HEADER = "token";
    public static final String LOBBYID_PATHVARIABLE = "lobbyId";

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
        lobbyService.assignRoles(lobby);
        // TODO close lobby & test for closed lobby in LobbyController
        Game game = gameService.createNewGame(lobby);
        gameService.createGameEmitter(game);
        // TODO handle IOException
        lobbyService.sendEmitterUpdate(lobbyService.getLobbyEmitter(lobby), "", LobbySseEvent.game);
    }


    
}
