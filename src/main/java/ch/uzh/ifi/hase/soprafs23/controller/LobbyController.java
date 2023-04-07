package ch.uzh.ifi.hase.soprafs23.controller;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.logicmapper.LogicDTOMapper;
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
    public LobbyGetDTO createNewLobby(@RequestHeader("uid") Long userId) {
        User user = userService.getUser(userId);
        Lobby l = lobbyService.createNewLobby(user);
        lobbyService.createLobbyEmitter(l);
        lobbyService.createVoiceChannelToken(l);
        return LogicDTOMapper.convertLobbyToLobbyGetDTO(l);
    }

    private String lobbyToJSON(Lobby lobby) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(LogicDTOMapper.convertLobbyToLobbyGetDTO(lobby));
    }

    @PutMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void joinLobby(@PathVariable("lobbyId") Long LobbyId, @RequestHeader("uid") Long userId) throws JsonProcessingException, IOException {
        User user = userService.getUser(userId);
        Lobby lobby = lobbyService.getLobbyById(LobbyId);
        lobbyService.joinUserToLobby(user, lobby);
        lobbyService.sendEmitterUpdate(lobbyService.getLobbyEmitter(lobby), lobbyToJSON(lobby));
    }

    @GetMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobbyInformation(@PathVariable("lobbyId") Long LobbyId, @RequestHeader("uid") Long userId) {
        User user = userService.getUser(userId);
        Lobby lobby = lobbyService.getLobbyById(LobbyId);
        lobbyService.validateUserIsInLobby(user, lobby);
        return LogicDTOMapper.convertLobbyToLobbyGetDTO(lobby);
    }

    @GetMapping("/lobbies/{lobbyId}/sse")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getLobbySseEmitterToken(@PathVariable("lobbyId") Long LobbyId, @RequestHeader("uid") Long userId) {
        User user = userService.getUser(userId);
        Lobby lobby = lobbyService.getLobbyById(LobbyId);
        lobbyService.validateUserIsInLobby(user, lobby);
        return lobbyService.getLobbyEmitterToken(lobby);
    }

    @GetMapping("/lobbies/{lobbyId}/sse/{token}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SseEmitter getLobbySseEmitter(@PathVariable("lobbyId") Long LobbyId, @PathVariable("token") String token) {
        Lobby lobby = lobbyService.getLobbyById(LobbyId);
        lobbyService.validateLobbyEmitterToken(lobby, token);
        return lobbyService.getLobbyEmitter(lobby);
    }

    @GetMapping("/lobbies/{lobbyId}/channels")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getVoiceChannelToken(@PathVariable("lobbyId") Long lobbyId/*, @RequestHeader("token") String token*/){
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        return lobbyService.getLobbyVoiceToken(lobby);
    }
}
