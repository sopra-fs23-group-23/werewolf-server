package ch.uzh.ifi.hase.soprafs23.logic.lobby;

import java.util.*;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.game.Scheduler;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.RandomTiedPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Fraction;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Mayor;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Villager;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Werewolf;

public class Lobby {
    private Long id;
    private Player admin;
    private Set<Player> players;
    private List<LobbyObserver> observers = new ArrayList<>();
    private Map<Class<? extends Role>, Role> roles;
    private boolean open;

    // TODO temporary on 3, change to 5
    public static final int MIN_SIZE = 3;
    public static final int MAX_SIZE = 20;

    public Lobby(Long id, Player admin) {
        this.id = id;
        this.admin = admin;
        this.players = new HashSet<>();
        players.add(admin);
        this.open = true;
        this.roles = new HashMap<>();
    }

    public void addObserver(LobbyObserver observer) {
        observers.add(observer);
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

    /**
     * @pre player is in lobby
     * @param id
     * @return
     */
    public Player getPlayerById(Long id) {
        Optional<Player> player = players.stream().filter(p -> p.getId().equals(id)).findFirst();
        assert player.isPresent();
        return player.get();
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
        Mayor mayor = new Mayor(this::getAlivePlayers, this::addPlayerToRole, new RandomTiedPollDecider(), Scheduler.getInstance());
        roles.put(Mayor.class, mayor);
        roles.put(Villager.class, new Villager(this::addPlayerToRole, this::getAlivePlayers, mayor));

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

    public List<Fraction> getFractions() {
        return roles.values().stream()
                .filter(Fraction.class::isInstance)
                .map(Fraction.class::cast)
                .toList();
    }

    public void dissolve() {
        observers.forEach((o) -> o.onLobbyDissolved(this));
    }
}
