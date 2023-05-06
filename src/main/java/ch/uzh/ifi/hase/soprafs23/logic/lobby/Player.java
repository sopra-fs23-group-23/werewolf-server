package ch.uzh.ifi.hase.soprafs23.logic.lobby;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.instantpollcommand.PrivateInstantPollCommand;

public class Player {
    private Long id;
    private String name;
    private boolean alive = true;
    private List<PlayerObserver> observers = new ArrayList<>();
    private List<PrivateInstantPollCommand> privatePollCommands = new ArrayList<>();
    private final String avatarUrl;

    public Player(Long id, String name) {
        this.id = id;
        this.name = name;
        // alternatives to miniavas: https://www.dicebear.com/guides/how-many-unique-avatars
        this.avatarUrl = "https://api.dicebear.com/6.x/miniavs/svg?seed=" + id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAlive() {
        return alive;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void addObserver(PlayerObserver observer) {
        observers.add(observer);
    }

    public void killPlayer() {
        this.alive = false;
        notifyObservers();
    }

    private void notifyObservers() {
        for (PlayerObserver playerObserver : observers) {
            playerObserver.onPlayerKilled();
        }
    }

    public void addPrivatePollCommand(PrivateInstantPollCommand privateInstantPollCommand) {
        privatePollCommands.add(privateInstantPollCommand);
    }

    public List<PrivateInstantPollCommand> getPrivatePollCommands() {
        return privatePollCommands;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Player other = (Player) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
