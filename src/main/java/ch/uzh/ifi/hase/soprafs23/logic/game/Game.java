package ch.uzh.ifi.hase.soprafs23.logic.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Supplier;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.DayVoter;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.NightVoter;

public class Game implements StageObserver{
    private Lobby lobby;
    private Stage currentStage;
    private int stageCount = 0;
    private List<PollCommand> lastStagePollCommands = new ArrayList<>();
    private List<GameObserver> observers = new ArrayList<>();

    /**
     * @pre lobby.getLobbySize() <= Lobby.MAX_SIZE && lobby.getLobbySize() >= Lobby.MIN_SIZE && lobby roles assigned
     * @param lobby
     */
    public Game(Lobby lobby) {
        assert lobby.getLobbySize() <= Lobby.MAX_SIZE && lobby.getLobbySize() >= Lobby.MIN_SIZE;
        this.lobby = lobby;
    }

    private Stage calculateNextStage() {
        // normal cases
        if (stageCount % 2 == 0) {
            return new Stage(StageType.Day, getDayVoters());
        } else {
            return new Stage(StageType.Night, getNightVoters());
        }
    }

    public void startGame() {
        startNextStage(calculateNextStage());
    }

    private void startNextStage(Stage nextStage) {
        stageCount++;
        currentStage = nextStage;
        currentStage.startStage();
        observers.stream().forEach(o -> o.onNewStage(this));
    }

    public Lobby getLobby() {
        return lobby;
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

    public List<PollCommand> getLastStagePollCommands() {
        return lastStagePollCommands;
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    @Override
    public void onStageFinished() {
        lastStagePollCommands = currentStage.getPollCommands();
        lastStagePollCommands.stream().forEach(p->p.execute());
        startNextStage(calculateNextStage());
    }

    private Queue<Supplier<Optional<Poll>>> getDayVoters() {
        Queue<Supplier<Optional<Poll>>> pq = new LinkedList<>();
        lobby.getRoles().stream()
            .filter(DayVoter.class::isInstance)
            .sorted()
            .map(DayVoter.class::cast)
            .forEach(voter -> pq.add(voter::createDayPoll));
        return pq;
    }

    private Queue<Supplier<Optional<Poll>>> getNightVoters() {
        Queue<Supplier<Optional<Poll>>> pq = new LinkedList<>();
        lobby.getRoles().stream()
            .filter(NightVoter.class::isInstance)
            .sorted()
            .map(NightVoter.class::cast)
            .forEach(voter -> pq.add(voter::createNightPoll));
        return pq;
    }

    @Override
    public void onNewPoll(Poll poll) {
        observers.stream().forEach(o -> o.onNewPoll(this, poll));   
    }
    
}
