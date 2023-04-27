package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.service.AgoraService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static ch.uzh.ifi.hase.soprafs23.service.LobbyService.LOBBYID_PATHVARIABLE;
import static ch.uzh.ifi.hase.soprafs23.service.UserService.USERAUTH_HEADER;


@RestController
public class AgoraController {

    private final UserService userService;
    private final AgoraService agoraService;
    private final LobbyService lobbyService;
    public AgoraController(AgoraService agoraService, LobbyService lobbyService, UserService userService){
        this.userService = userService;
        this.agoraService = agoraService;
        this.lobbyService = lobbyService;
    }

    @GetMapping("/agora/{lobbyId}/token")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getVoiceChannelToken(@PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId, @RequestHeader(USERAUTH_HEADER) String userToken){
        User user = userService.getUserByToken(userToken);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateUserIsInLobby(user, lobby);
        return agoraService.createVoiceChannelToken(lobby, user);
    }

    @PostMapping("/agora/{lobbyId}/rules/audio/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void forceMutePlayerFromChannel(@PathVariable("lobbyId") Long lobbyId, @PathVariable("userId") Long userId, @RequestHeader(USERAUTH_HEADER) String userToken) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        User user = userService.getUserByToken(userToken);
        User userToMute = userService.getUser(userId);
        Player player = lobby.getAdmin();

        // checks if user is in lobby
        lobbyService.validateUserIsInLobby(userToMute, lobby);
        // check if user who sent request is admin
        userService.validateUserIsPlayer(user,player);
        // get player
        Player playerToMute = lobby.getPlayerById(userId);
        // mutes the user
        agoraService.muteTroll(playerToMute);
    }
}
