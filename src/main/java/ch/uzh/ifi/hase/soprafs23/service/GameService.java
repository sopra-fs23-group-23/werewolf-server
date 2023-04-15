package ch.uzh.ifi.hase.soprafs23.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.service.wrapper.EmitterWrapper;

@Service
@Transactional
public class GameService {
    private Map<Long, Game> games = new HashMap<>();
    private Map<Long, EmitterWrapper> gameEmitterMap = new HashMap<>();

    /**
     * @pre lobby.getLobbySize() <= Lobby.MAX_SIZE && lobby.getLobbySize() >= Lobby.MIN_SIZE && lobby roles assigned
     * @param lobby
     */
    public Game createNewGame(Lobby lobby) {
        assert lobby.getLobbySize() <= Lobby.MAX_SIZE && lobby.getLobbySize() >= Lobby.MIN_SIZE;
        Game game = new Game(lobby);
        games.put(lobby.getId(), game);
        return game;
    }

    public Game getGame(Lobby lobby) {
        if (!games.containsKey(lobby.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No game found for lobby with id %d", lobby.getId()));
        }
        return games.get(lobby.getId());
    }

    public SseEmitter createGameEmitter(Game game) {
        SseEmitter emitter = new SseEmitter(-1l);
        gameEmitterMap.put(game.getLobby().getId(), new EmitterWrapper(emitter, UUID.randomUUID().toString()));
        return emitter;
    }

    public SseEmitter getGameEmitter(Game game) {
        return gameEmitterMap.get(game.getLobby().getId()).getEmitter();
    }
}
