package ch.uzh.ifi.hase.soprafs23.logic.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.role.FractionRole;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Mayor;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Villager;
import ch.uzh.ifi.hase.soprafs23.logic.role.gameroles.Werewolf;
import ch.uzh.ifi.hase.soprafs23.logic.role.stagevoter.DayVoter;

class GameIntegrationTest {
    // TODO: unit test rest of Game class

    private class MockPollFunction {
        private boolean called = false;
        private List<Role> callers = new ArrayList<>();

        public Supplier<Optional<Poll>> mockFunction(Role r) {
            callers.add(r);
            called = true;
            return ((DayVoter)r)::createDayPoll;
        }

        public boolean isCalled() {
            return called;
        }

        public List<Role> getCallers() {
            return callers;
        }
    }

    private class MockGameObserver implements GameObserver {
        private final Game expectedGame;
        private boolean onNewStageCalled = false;
        private boolean onGameFinishedCalled = false;

        public MockGameObserver(Game expectedGame) {
            this.expectedGame = expectedGame;
        }

        @Override
        public void onNewPoll(Game game) {
            assertEquals(expectedGame, game);
        }

        @Override
        public void onNewStage(Game game) {
            assertEquals(expectedGame, game);
            onNewStageCalled = true;
        }

        @Override
        public void onGameFinished(Game game) {
            assertEquals(expectedGame, game);
            onGameFinishedCalled = true;
        }

        @Override
        public void onPlayerDiedUnrevivable(Game game, Player player) {
            assertEquals(expectedGame, game);
        }
    }

    private Lobby lobby;
    private Game game;
    private MockGameObserver mockGameObserver;

    @BeforeEach
    public void setUp() {
        lobby = mock(Lobby.class);
        game = new Game(lobby);
        mockGameObserver = new MockGameObserver(game);
        game.addObserver(mockGameObserver);
    }

    @Test
    void testGameOneStage() {
        FractionRole fraction = mock(FractionRole.class);
        when(lobby.getFractions()).thenReturn(List.of(fraction));
        when(fraction.hasWon()).thenReturn(true);
        game.startGame();
        Stage stage = game.getCurrentStage();
        assertEquals(StageType.Day, stage.getType());
        assertTrue(mockGameObserver.onNewStageCalled);
        assertTrue(mockGameObserver.onGameFinishedCalled);
        assertTrue(game.isFinished());
        assertEquals(fraction, game.getWinner());
    }

    @Test
    void testGameTwoStage() {
        FractionRole fraction = mock(FractionRole.class);
        when(lobby.getFractions()).thenReturn(List.of(fraction));
        when(fraction.hasWon()).thenReturn(false, true);
        game.startGame();
        Stage stage = game.getCurrentStage();
        assertEquals(StageType.Night, stage.getType());
        assertTrue(mockGameObserver.onNewStageCalled);
        assertTrue(mockGameObserver.onGameFinishedCalled);
        assertTrue(game.isFinished());
        assertEquals(fraction, game.getWinner());
    }

    @Test
    void testGetVotersOfType() {
        Role r1 = new Villager(null, null, null);
        Role r2 = new Werewolf(null);
        Role r3 = new Mayor(null, null, null);

        MockPollFunction mockPollFunction = new MockPollFunction();

        Game.getVotersOfType(List.of(r1, r2, r3), DayVoter.class, mockPollFunction::mockFunction);
        assertTrue(mockPollFunction.isCalled());
        assertEquals(List.of(r3, r1), mockPollFunction.getCallers());
    }
}
