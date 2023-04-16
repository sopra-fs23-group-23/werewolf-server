package ch.uzh.ifi.hase.soprafs23.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ch.uzh.ifi.hase.soprafs23.constant.sse.GameSseEvent;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.service.helper.EmitterHelper;
import ch.uzh.ifi.hase.soprafs23.service.wrapper.GameEmitter;

@Service
@Transactional
public class GameService {
    private Map<Long, Game> games = new HashMap<>();
    private Map<Long, GameEmitter> gameEmitterMap = new HashMap<>();

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

    public GameEmitter createGameEmitter(Game game) {
        GameEmitter gameEmitter = new GameEmitter(game);
        gameEmitterMap.put(game.getLobby().getId(), gameEmitter);
        return gameEmitter;
    }

    /**
     * @pre gameEmitterMap.containsKey(game.getLobby().getId())
     * @param game
     * @return
     */
    public GameEmitter getGameEmitter(Game game) {
        assert gameEmitterMap.containsKey(game.getLobby().getId());
        return gameEmitterMap.get(game.getLobby().getId());
    }

    /**
     * @pre game lobby contains user
     * @param game
     * @param user
     * @return
     */
    public SseEmitter getPlayerSseEmitter(Game game, User user) {
        return gameEmitterMap.get(game.getLobby().getId()).getPlayerEmitter(user.getId());
    }

    public void sendEmitterUpdate(SseEmitter emitter, String data, GameSseEvent gameSseEvent) {
        EmitterHelper.sendEmitterUpdate(emitter, data, gameSseEvent.toString());
    }

    public void sendGameEmitterUpdate(GameEmitter gameEmitterWrapper, String data, GameSseEvent event) {
        Consumer<SseEmitter> action = new Consumer<SseEmitter>() {
            @Override
            public void accept(SseEmitter t) {
                sendEmitterUpdate(t, data, event);
            }
        };
        gameEmitterWrapper.forAllPlayerEmitters(action);
    }

    public void startGame(Game game) {
        game.startGame();
    }

    public void schedule(Runnable command, int delaySeconds) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(command, delaySeconds, TimeUnit.SECONDS);
    }
}
