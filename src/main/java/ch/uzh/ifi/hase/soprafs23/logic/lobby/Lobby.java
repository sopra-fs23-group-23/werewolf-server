package ch.uzh.ifi.hase.soprafs23.logic.lobby;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.role.FractionRole;
import ch.uzh.ifi.hase.soprafs23.logic.role.FractionRoleComparator;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.game.Scheduler;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.RandomTiedPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Cupid;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Hunter;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Lover;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Mayor;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Seer;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Villager;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Werewolf;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Witch;

public class Lobby {
    private Long id;
    private Player admin;
    private Set<Player> players;
    private List<LobbyObserver> observers = new ArrayList<>();
    private Map<Class<? extends Role>, Role> roles;
    private boolean open;
    private int partyVoteDurationSeconds = 90;
    private int singleVoteDurationSeconds = 15;

    public static final int MIN_SIZE = 5;
    public static final int MAX_SIZE = 20;
    public static final int MIN_PARTY_VOTE_DURATION_SECONDS = 15;
    public static final int MAX_PARTY_VOTE_DURATION_SECONDS = 180;
    public static final int MIN_SINGLE_VOTE_DURATION_SECONDS = 15;
    public static final int MAX_SINGLE_VOTE_DURATION_SECONDS = 60;

    public Lobby(Long id, Player admin) {
        this.id = id;
        this.admin = admin;
        this.players = new HashSet<>();
        players.add(admin);
        this.open = true;
        this.roles = new HashMap<>();
    }

    public boolean isObserver(LobbyObserver observer) {
        return observers.contains(observer);
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
        players.add(player);
    }

    public void removePlayer(Player player) {
        if(!players.remove(player)) {
            throw new IllegalArgumentException(String.format("Player with user id %d is not in Lobby and could not be removed.", player.getId()));
        }
    }

    public void reInstatiatePlayers() {
        // this is a dirty fix, but too lazy to refactor design...
        List<Player> newPlayers = new ArrayList<>();
        for (Player player : players) {
            newPlayers.add(new Player(player.getId(), player.getName()));
        }
        players = new HashSet<>(newPlayers);
    }

    public Collection<Player> getPlayers() {
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

    public Collection<Player> getPlayersByRole(Class<? extends Role> roleClass) {
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

    public int getPartyVoteDurationSeconds() {
        return partyVoteDurationSeconds;
    }

    /**
     * @pre MIN_PARTY_VOTE_DURATION_SECONDS <= partyVoteDurationSeconds <= MAX_PARTY_VOTE_DURATION_SECONDS
     * @param partyVoteDurationSeconds
     */
    public void setPartyVoteDurationSeconds(int partyVoteDurationSeconds) {
        this.partyVoteDurationSeconds = partyVoteDurationSeconds;
    }

    public int getSingleVoteDurationSeconds() {
        return singleVoteDurationSeconds;
    }

    /**
     * @pre MIN_SINGLE_VOTE_DURATION_SECONDS <= singleVoteDurationSeconds <= MAX_SINGLE_VOTE_DURATION_SECONDS
     * @param singleVoteDurationSeconds
     */
    public void setSingleVoteDurationSeconds(int singleVoteDurationSeconds) {
        this.singleVoteDurationSeconds = singleVoteDurationSeconds;
    }

    public void instantiateRoles(
        Supplier<List<Player>> alivePlayersSupplier,
        BiConsumer<Player, Class<? extends Role>> addPlayerToRoleConsumer,
        Supplier<List<PollCommand>> currentStagePollCommandsSupplier,
        Consumer<PollCommand> removePollCommandConsumer,
        Consumer<PollCommand> addPollCommandConsumer,
        Function<Player, Collection<Role>> getRolesOfPlayerFunction
    ) {
        roles.put(Werewolf.class, new Werewolf(partyVoteDurationSeconds, alivePlayersSupplier));
        Mayor mayor = new Mayor(singleVoteDurationSeconds, alivePlayersSupplier, new RandomTiedPollDecider(), Scheduler.getInstance());
        roles.put(Mayor.class, mayor);
        roles.put(Witch.class, new Witch(singleVoteDurationSeconds, alivePlayersSupplier, currentStagePollCommandsSupplier, removePollCommandConsumer));
        roles.put(Hunter.class, new Hunter(singleVoteDurationSeconds, alivePlayersSupplier));
        roles.put(Villager.class, new Villager(partyVoteDurationSeconds, addPlayerToRoleConsumer, alivePlayersSupplier, mayor));
        roles.put(Cupid.class, new Cupid(singleVoteDurationSeconds, alivePlayersSupplier, addPlayerToRoleConsumer));
        roles.put(Lover.class, new Lover(alivePlayersSupplier, addPollCommandConsumer));
        roles.put(Seer.class, new Seer(singleVoteDurationSeconds, alivePlayersSupplier, getRolesOfPlayerFunction));
    }

    private void addSpecialVillagerRoles(Map<Class<? extends Role>, List<Player>> mapOfPlayersPerRole, List<Player> villagers) {
        mapOfPlayersPerRole.put(Cupid.class, List.of(villagers.get(0)));
        mapOfPlayersPerRole.put(Witch.class, List.of(villagers.get(1)));
        mapOfPlayersPerRole.put(Hunter.class, List.of(villagers.get(2)));
        mapOfPlayersPerRole.put(Seer.class, List.of(villagers.get(3)));
    }


    /**
     * @pre roles instantiated
     */
    public void assignRoles() {
        ArrayList<Player> playerList = shufflePlayers();

        Map<Class<? extends Role>, List<Player>> mapOfPlayersPerRole = new HashMap<>();

        mapOfPlayersPerRole.put(Werewolf.class, playerList.subList(0, this.getLobbySize() / 3));
        List<Player> villagers = playerList.subList(this.getLobbySize() / 3, this.getLobbySize());
        mapOfPlayersPerRole.put(Villager.class, villagers);
        addSpecialVillagerRoles(mapOfPlayersPerRole, villagers);
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

    public List<FractionRole> getFractions() {
        return roles.values().stream()
                .filter(FractionRole.class::isInstance)
                .map(FractionRole.class::cast)
                .sorted(new FractionRoleComparator())
                .toList();
    }

    public void dissolve() {
        observers.forEach((o) -> o.onLobbyDissolved(this));
    }
}
