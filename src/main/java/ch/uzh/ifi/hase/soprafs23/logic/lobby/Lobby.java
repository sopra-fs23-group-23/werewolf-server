package ch.uzh.ifi.hase.soprafs23.logic.lobby;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class Lobby {
    private String id;
    private Player admin;
    private List<Player> players;
    private Map<Class<? extends Role>, Role> roles;
    private boolean open;

    public Lobby(String id, Player admin) {
        this.id = id;
        this.admin = admin;
    }

    public static int getLobbySize(Lobby lobby){
        int playerCount = 0;
        for (Player player: lobby.players) {
            playerCount++;
        }
        return playerCount;
    }

    public void addPlayer(Player player) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addPlayer'");
    }

    public void removePlayer(Player player) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removePlayer'");
    }

    public Collection<Role> getRoles() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoles'");
    }

    public Collection<Role> getRolesOfPlayer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRolesOfPlayer'");
    }

    public Iterable<Player> getPlayersByRole(Class<? extends Role> roleClass) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlayersByRole'");
    }

    public Iterable<Player> getAlivePlayers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAlivePlayers'");
    }

    public void addPlayerToRole(Player player, Class<? extends Role> role) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addPlayerToRole'");
    }

    public void instantiateRoles(Game game) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removePlayer'");
    }

    
}
