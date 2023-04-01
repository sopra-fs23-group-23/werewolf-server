package ch.uzh.ifi.hase.soprafs23.logic.game;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.FirstDayVoter;

public class Game implements StageObserver{
    private Lobby lobby;
    private Stage currentStage;

    private final static int minLobbySize = 5;
    private final static int maxLobbySize = 20;

    public Game(Lobby lobby) {
        int lobbySize = Lobby.getLobbySize(lobby);
        // check if lobby has at least 5 and at most 20 players
        if (minLobbySize > lobbySize || lobbySize > maxLobbySize){
            throw new IllegalStateException("Lobby size either to big or to small to create game instance.");
        }
        this.lobby = lobby;
    }

    public void startGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startGame'");
    }

    public void createAndStartNextStage() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAndStartNextStage'");
    }

    public Lobby getLobby() {
        return lobby;
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

    @Override
    public void onStageFinished() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onStageFinished'");
    }

    private Queue<Supplier<Poll>> getFirstDayVoters() {
        // TODO This is an example how to implement these methods, verify
        Queue<Supplier<Poll>> pq = new LinkedList<>();
        lobby.getRoles().stream()
            .filter(FirstDayVoter.class::isInstance)
            .sorted()
            .map(FirstDayVoter.class::cast)
            .forEach(voter -> pq.add(voter::createFirstDayPoll));
        return pq;
    }
    
}
