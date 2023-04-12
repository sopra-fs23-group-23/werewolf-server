package ch.uzh.ifi.hase.soprafs23.rest.logicmapper;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;

public final class LogicEntityMapper {
    private LogicEntityMapper(){}

    public static Player createPlayerFromUser(User user) {
        return new Player(user.getId(), user.getUsername());
    }
}
