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
import ch.uzh.ifi.hase.soprafs23.logic.role.Fraction;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.DayVoter;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.FirstDayVoter;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.NightVoter;

public class Game implements StageObserver{
    private Lobby lobby;
    private Stage currentStage;
    private boolean started = false;
    private Optional<Poll> currentPoll = Optional.empty();
    private Optional<Fraction> winner = Optional.empty();
    private int stageCount = 0;
    private boolean finished = false;
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

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    private Stage calculateNextStage() {
        // special cases
        if (stageCount == 1) {
            // first day
            return new Stage(StageType.Day, getFirstDayVoters());
        }
        // normal cases
        if (stageCount % 2 == 0) {
            return new Stage(StageType.Night, getNightVoters());
        } else {
            return new Stage(StageType.Day, getDayVoters());
        }
    }

    public void startGame() {
        startNextStage(calculateNextStage());
        started = true;
    }

    public boolean isStarted() {
        return started;
    }

    private void startNextStage(Stage nextStage) {
        stageCount++;
        currentStage = nextStage;
        currentStage.addObserver(this);
        currentStage.startStage();
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

    public Poll getCurrentPoll() throws IllegalStateException{
        if (currentPoll.isEmpty()) {
            throw new IllegalStateException("No poll is currently running");
        }
        return currentPoll.get();
    }

    public Fraction getWinner() {
        if (winner.isEmpty()) {
            throw new IllegalStateException("Game is not finished yet");
        }
        return winner.get();
    }

    @Override
    public void onStageFinished() {
        lastStagePollCommands = currentStage.getPollCommands();
        lastStagePollCommands.stream().forEach(p->p.execute());
        for (Fraction fraction : lobby.getFractions()) {
            if(fraction.hasWon()) {
                finishGame(fraction);
                return;
            }
        }
        startNextStage(calculateNextStage());
    }

    private void finishGame(Fraction winningFraction) {
        winner = Optional.of(winningFraction);
        finished = true;
        observers.stream().forEach(observer->observer.onGameFinished(this));
    }

    public boolean isFinished() {
        return finished;
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

    private Queue<Supplier<Optional<Poll>>> getFirstDayVoters() {
        Queue<Supplier<Optional<Poll>>> pq = new LinkedList<>();
        lobby.getRoles().stream()
            .filter(FirstDayVoter.class::isInstance)
            .sorted()
            .map(FirstDayVoter.class::cast)
            .forEach(voter -> pq.add(voter::createFirstDayPoll));
        pq.addAll(getDayVoters());
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
        currentPoll = Optional.of(poll);
        observers.forEach(o -> o.onNewPoll(this));
    }
    
}
