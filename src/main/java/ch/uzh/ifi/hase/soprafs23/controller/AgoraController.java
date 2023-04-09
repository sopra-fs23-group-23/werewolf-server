package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.Reason;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.service.AgoraService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

import ch.uzh.ifi.hase.soprafs23.service.LobbyService;

import static ch.uzh.ifi.hase.soprafs23.controller.LobbyController.LOBBYID_PATHVARIABLE;
import static ch.uzh.ifi.hase.soprafs23.controller.LobbyController.USERAUTH_HEADER;
import static java.lang.Long.parseLong;
import static javax.xml.bind.DatatypeConverter.parseInt;


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

    // TODO: Secure Endpoint, so that just admin can call it correctly
    @PostMapping("agora/rules/forceMute/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void forceMutePlayerFromChannel(@PathVariable("userId") Long userId) throws IOException, InterruptedException {
        Player player = new Player(userId, "willy");
        agoraService.muteTroll(player);
    }

    // TODO we tested them but dont need actual endpoints in the controller
    /*
    @PostMapping("/rules/kickVillager/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void kickVillager(@PathVariable("userId") Long userId) throws IOException, InterruptedException {
        Player player = new Player(userId, "willy");
        agoraService.kickVillager(player);
    }

    @PostMapping("/rules/muteDead/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void muteDeadPlayer(@PathVariable("userId") Long userId) throws IOException, InterruptedException {
        Player player = new Player(userId, "willy");
        agoraService.muteDeadPlayer(player);
    }

    @PostMapping("/kickAll")
    @ResponseStatus(HttpStatus.OK)
    public void kickAll() throws IOException, InterruptedException {

        agoraService.kickAll("123456");
    }

    @DeleteMapping("/rules/")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRules() throws IOException, InterruptedException {
        Player player = new Player((long) 1, "roby");
        agoraService.deleteRules(Reason.MUTE_DEAD, Optional.of(player));
    }
    */

}
