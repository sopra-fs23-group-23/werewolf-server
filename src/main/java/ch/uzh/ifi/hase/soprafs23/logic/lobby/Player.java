package ch.uzh.ifi.hase.soprafs23.logic.lobby;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String id;
    private String name;
    private boolean alive = true;
    private List<PlayerObserver> observers = new ArrayList<>();

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAlive() {
        return alive;
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
}
