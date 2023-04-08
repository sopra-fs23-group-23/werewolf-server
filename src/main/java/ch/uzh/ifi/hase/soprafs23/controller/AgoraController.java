package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.service.AgoraService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import ch.uzh.ifi.hase.soprafs23.service.LobbyService;


@RestController
public class AgoraController {

    private final AgoraService agoraService;
    private final LobbyService lobbyService;
    public AgoraController(AgoraService agoraService, LobbyService lobbyService){
        this.agoraService = agoraService;
        this.lobbyService = lobbyService;
    }

    @PostMapping("/rules/audio/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void forceMutePlayerFromChannel(@PathVariable("userId") Long userId) throws IOException, InterruptedException {
        agoraService.muteTroll(userId.toString());
    }

    // TODO we tested them but dont need actual endpoints in the controller
//    @PostMapping("/rules/join/{userId}")
//    @ResponseStatus(HttpStatus.OK)
//    public void kickVillager(@PathVariable("userId") Long userId) throws IOException, InterruptedException {
//        agoraService.kickVillager(userId.toString());
//    }
//
//    @PostMapping("/rules/boing/{userId}")
//    @ResponseStatus(HttpStatus.OK)
//    public void muteDeadPlayer(@PathVariable("userId") Long userId) throws IOException, InterruptedException {
//        agoraService.muteDeadPlayer(userId.toString());
//    }
//
//    @PostMapping("/rules/egg")
//    @ResponseStatus(HttpStatus.OK)
//    public void kickAll() throws IOException, InterruptedException {
//
//        agoraService.kickAll("123456");
//    }
}
