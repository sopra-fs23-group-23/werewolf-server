package ch.uzh.ifi.hase.soprafs23.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;

/**
 * This class handles all requests related to lobby
 */

@RestController
public class LobbyController {

    private final UserService userService;
    private final LobbyService lobbyService;

    public LobbyController(UserService userService, LobbyService lobbyService) {
        this.userService = userService;
        this.lobbyService = lobbyService;
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Long createNewLobby(@RequestHeader("uid") Long userId) {
        User user = userService.getUser(userId);
        Lobby l = lobbyService.createNewLobby(user);
        return l.getId();
    }

    @PutMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void joinLobby(@PathVariable("lobbyId") Long LobbyId, @RequestHeader("uid") Long userId) {
        User user = userService.getUser(userId);
        Lobby lobby = lobbyService.getLobbyById(LobbyId);
        lobbyService.joinUserToLobby(user, lobby);
    }
}
