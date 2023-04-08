package ch.uzh.ifi.hase.soprafs23.service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import ch.uzh.ifi.hase.soprafs23.agora.RTCTokenBuilder;
import ch.uzh.ifi.hase.soprafs23.constant.VoiceChatRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.rest.logicmapper.LogicEntityMapper;
import ch.uzh.ifi.hase.soprafs23.service.wrapper.LobbyEmitterWrapper;

@Service
@Transactional
public class LobbyService {

    private Map<Long, Lobby> lobbies = new HashMap<>();
    private Map<Long, LobbyEmitterWrapper> lobbyEmitterMap = new HashMap<>();

    private Long createLobbyId() {
        Long newId = ThreadLocalRandom.current().nextLong(100000, 999999);
        if (lobbies.containsKey(newId)) {
            return createLobbyId();
        }
        return newId;
    }

    public Lobby createNewLobby(User creator) {
        Player admin = LogicEntityMapper.createPlayerFromUser(creator);
        if(lobbies.values().stream().anyMatch(l -> l.getAdmin().equals(admin))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already has a lobby");
        }
        Lobby l = new Lobby(createLobbyId(), admin);
        lobbies.put(l.getId(), l);
        return l;
    }

    public Collection<Lobby> getLobbies() {
        return lobbies.values();
    }

    public Lobby getLobbyById(Long lobbyId) {
        if (!lobbies.containsKey(lobbyId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Lobby with id %d does not exist", lobbyId));
        }
        return lobbies.get(lobbyId);
    }

    private boolean userInALobby(User user) {
        return lobbies.values().stream().anyMatch(
            l -> StreamSupport.stream(l.getPlayers().spliterator(), false).anyMatch(p->p.getId()==user.getId())
        );
    }

    private boolean userIsInLobby(User user, Lobby lobby) {
        return StreamSupport.stream(lobby.getPlayers().spliterator(), false).anyMatch(p->p.getId()==user.getId());
    }

    public void validateUserIsInLobby(User user, Lobby lobby) {
        if (!userIsInLobby(user, lobby)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not part of this lobby");
        }
    }

    public void joinUserToLobby(User user, Lobby lobby) {
        if (lobby.getLobbySize() >= Lobby.MAX_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby is already full.");
        }
        if (!lobby.isOpen()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby is closed.");
        }
        if (StreamSupport.stream(lobby.getPlayers().spliterator(), false).anyMatch(p->p.getId()==user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already in this lobby.");
        }
        if (userInALobby(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already in a lobby.");
        }
        lobby.addPlayer(LogicEntityMapper.createPlayerFromUser(user));
    }

    public SseEmitter createLobbyEmitter(Lobby lobby) {
        SseEmitter emitter = new SseEmitter(-1l);
        lobbyEmitterMap.put(lobby.getId(), new LobbyEmitterWrapper(emitter, UUID.randomUUID().toString()));
        return emitter;
    }

    public String getLobbyEmitterToken (Lobby lobby) {
        return lobbyEmitterMap.get(lobby.getId()).getToken();
    }

    public void validateLobbyEmitterToken (Lobby lobby, String token) {
        if(!lobbyEmitterMap.get(lobby.getId()).getToken().equals(token)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid token");
        }
    }

    public SseEmitter getLobbyEmitter (Lobby lobby) {
        return lobbyEmitterMap.get(lobby.getId()).getEmitter();
    }

    public void sendEmitterUpdate(SseEmitter emitter, String data) throws IOException {
        SseEventBuilder event;
        event = SseEmitter.event()
            .data( data + "\n", MediaType.APPLICATION_JSON)
            .id(UUID.randomUUID().toString())
            .name("lobby update event");
            emitter.send(event);
    }

    public String createVoiceChannelToken(Lobby lobby, User user) {
        RTCTokenBuilder newToken = new RTCTokenBuilder();
        String token = newToken.buildTokenWithUserAccount(lobby.getId().toString(), user.getId().toString(), VoiceChatRole.Role_Publisher);
        if (Objects.equals(token, "")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return token;
    }
}
