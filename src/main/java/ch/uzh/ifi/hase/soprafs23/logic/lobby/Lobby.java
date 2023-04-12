package ch.uzh.ifi.hase.soprafs23.logic.lobby;

import java.util.*;
import java.util.stream.StreamSupport;

import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.NullResultPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Villager;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Werewolf;

public class Lobby {
    private Long id;
    private Player admin;
    private Set<Player> players;
    private Map<Class<? extends Role>, Role> roles;
    private boolean open;

    public static final int MIN_SIZE = 5;
    public static final int MAX_SIZE = 20;

    public Lobby(Long id, Player admin) {
        this.id = id;
        this.admin = admin;
        this.players = new HashSet<>();
        players.add(admin);
        this.open = true;
        this.roles = new HashMap<>();
    }

    public int getLobbySize(){
        return this.players.size();
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * @pre getLobbySize() <= MAX_SIZE && isOpen() && !players.contains(player)
     * @param player
     */
    public void addPlayer(Player player) {
        assert getLobbySize() <= MAX_SIZE && isOpen() && !players.contains(player);
        players.add(player);
    }

    public void removePlayer(Player player) {
        if(!players.remove(player)) {
            throw new IllegalArgumentException(String.format("Player with user id %d is not in Lobby and could not be removed.", player.getId()));
        }
    }

    public Iterable<Player> getPlayers() {
        return players;
    }

    public Player getPlayerById(Long id){
        //I would move this to the service, otherways we have to check somehow the precondition that player is in this lobby
        return players.stream().filter(p -> p.getId() == id).findFirst().get();
    }

    public Player getAdmin() {
        return admin;
    }

    public Long getId() {
        return id;
    }

    public Collection<Role> getRoles() {
        return roles.values();
    }

    public Collection<Role> getRolesOfPlayer(Player player) {
        return roles.values().stream().filter(r->r.getPlayers().contains(player)).toList();
    }

    public Iterable<Player> getPlayersByRole(Class<? extends Role> roleClass) {
        return roles.get(roleClass).getPlayers();
    }

    public List<Player> getAlivePlayers() {
        ArrayList<Player> alivePlayers = new ArrayList<>();

        for (Player player : this.players){
            if (player.isAlive()){
                alivePlayers.add(player);
            }
        }
        return alivePlayers;
    }

    public void addPlayerToRole(Player player, Class<? extends Role> role) {
        roles.get(role).addPlayer(player);
    }

    public void instantiateRoles() {
        roles.put(Werewolf.class, new Werewolf(this::getAlivePlayers));
        roles.put(Villager.class, new Villager(this::addPlayerToRole, this::getAlivePlayers, new NullResultPollDecider()));

        ArrayList<Player> playerList = shufflePlayers();

        Map<Class<? extends Role>, List<Player>> mapOfPlayersPerRole = new HashMap<>();

        mapOfPlayersPerRole.put(Werewolf.class, playerList.subList(0, this.getLobbySize() / 3));
        mapOfPlayersPerRole.put(Villager.class, playerList.subList(this.getLobbySize() / 3, this.getLobbySize()));

        for (Map.Entry<Class<? extends Role>, List<Player>> entry : mapOfPlayersPerRole.entrySet()){
            for (Player player : entry.getValue()){
                addPlayerToRole(player, entry.getKey());
            }
        }
    }
    public ArrayList<Player> shufflePlayers(){
        // returns a shuffled list of all the players in the lobby
        ArrayList<Player> playerList = new ArrayList<>(this.players);
        Collections.shuffle(playerList);
        return playerList;
    }
}
