package ch.uzh.ifi.hase.soprafs23.controller;

import java.io.IOException;

import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
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
    public final static String USERAUTH_HEADER = "token";
    public final static String LOBBYID_PATHVARIABLE = "lobbyId";

    private final UserService userService;
    private final LobbyService lobbyService;
    private final UserRepository userRepository;

    public LobbyController(UserService userService, LobbyService lobbyService,
                           UserRepository userRepository) {
        this.userService = userService;
        this.lobbyService = lobbyService;
        this.userRepository = userRepository;
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createNewLobby(@RequestHeader(USERAUTH_HEADER) String userToken) {
        User user = userService.getUserByToken(userToken);
        Lobby l = lobbyService.createNewLobby(user);
        lobbyService.createLobbyEmitter(l);
        return LogicDTOMapper.convertLobbyToLobbyGetDTO(l);
    }

    private String lobbyToJSON(Lobby lobby) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(LogicDTOMapper.convertLobbyToLobbyGetDTO(lobby));
    }

    @PutMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void joinLobby(@PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId, @RequestHeader(USERAUTH_HEADER) String userToken) throws JsonProcessingException, IOException {
        User user = userService.getUserByToken(userToken);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.joinUserToLobby(user, lobby);
        lobbyService.sendEmitterUpdate(lobbyService.getLobbyEmitter(lobby), lobbyToJSON(lobby));
    }

    @GetMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobbyInformation(@PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId, @RequestHeader(USERAUTH_HEADER) String userToken) {
        User user = userService.getUserByToken(userToken);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateUserIsInLobby(user, lobby);
        return LogicDTOMapper.convertLobbyToLobbyGetDTO(lobby);
    }

    @GetMapping("/lobbies/{lobbyId}/sse")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getLobbySseEmitterToken(@PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId, @RequestHeader(USERAUTH_HEADER) String userToken) {
        User user = userService.getUserByToken(userToken);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateUserIsInLobby(user, lobby);
        return lobbyService.getLobbyEmitterToken(lobby);
    }

    @GetMapping("/lobbies/{lobbyId}/sse/{sseToken}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SseEmitter getLobbySseEmitter(@PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId, @PathVariable("sseToken") String sseToken) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateLobbyEmitterToken(lobby, sseToken);
        return lobbyService.getLobbyEmitter(lobby);
    }

    @GetMapping("/lobbies/{lobbyId}/channels")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getVoiceChannelToken(@PathVariable(LOBBYID_PATHVARIABLE) Long lobbyId, @RequestHeader(USERAUTH_HEADER) String userToken){
        User user = userRepository.findByToken(userToken);
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.validateUserIsInLobby(user, lobby);
        return lobbyService.createVoiceChannelToken(lobby, user);
    }
}
