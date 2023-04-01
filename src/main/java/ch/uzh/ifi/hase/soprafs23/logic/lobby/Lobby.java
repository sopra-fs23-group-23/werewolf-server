package ch.uzh.ifi.hase.soprafs23.logic.lobby;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class Lobby {
    private Long id;
    private Player admin;
    private Set<Player> players;
    private Map<Class<? extends Role>, Role> roles;
    private boolean open;

    public Lobby(Long id, Player admin) {
        this.id = id;
        this.admin = admin;
        this.players = new HashSet<>();
        players.add(admin);
        this.open = true;
    }

    public static int getLobbySize(Lobby lobby){
        int playerCount = 0;
        for (Player player: lobby.players) {
            playerCount++;
        }
        return playerCount;
    }

    public void addPlayer(Player player) {
        if(!players.add(player)) {
            throw new IllegalArgumentException(String.format("Player with user id %d is already in Lobby.", player.getId()));
        }
    }

    public void removePlayer(Player player) {
        if(!players.remove(player)) {
            throw new IllegalArgumentException(String.format("Player with user id %d is not in Lobby and could not be removed.", player.getId()));
        }
    }

    public Iterable<Player> getPlayers() {
        return players;
    }

    public Player getAdmin() {
        return admin;
    }

    public Long getId() {
        return id;
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
