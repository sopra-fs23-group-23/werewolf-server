package ch.uzh.ifi.hase.soprafs23.service.wrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public class GameEmitter {
    private Map<Long, SseEmitter> playerEmitter = new HashMap<>();

    

    public GameEmitter(Game game) {
        for (Player player : game.getLobby().getPlayers()) {
            addPlayerEmitter(player.getId(), new SseEmitter(-1l));
        }
    }

    /**
     * @pre !playerEmitter.containsKey(uid)
     * @param uid
     * @param emitter
     */
    private void addPlayerEmitter(Long uid, SseEmitter emitter) {
        assert !playerEmitter.containsKey(uid);
        playerEmitter.put(uid, emitter);
    }

    /**
     * @pre playerEmitter.contains(uid)
     * @param uid
     * @return
     */
    public SseEmitter getPlayerEmitter(Long uid) {
        assert playerEmitter.containsKey(uid);
        return playerEmitter.get(uid);
    }

    public void forAllPlayerEmitters(Consumer<SseEmitter> action) {
        playerEmitter.values().stream().forEach(action);
    }
}
