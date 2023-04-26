package ch.uzh.ifi.hase.soprafs23.logic.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;

public class StageTest {
    private class StageObserverMock implements StageObserver {
        private final Stage stage;
        private final Poll expectedPoll;
        private final List<PollCommand> expectedCommands;

        private boolean onStageFinishedCalled = false;
        private boolean onNewPollCalled = false;

        public StageObserverMock(Stage stage, Poll expectedPoll, List<PollCommand> expectedCommands) {
            this.stage = stage;
            this.expectedPoll = expectedPoll;
            this.expectedCommands = expectedCommands;
        }

        @Override
        public void onStageFinished() {
            assertThat("List equality without order", stage.getPollCommands(), containsInAnyOrder(expectedCommands.toArray()));
            onStageFinishedCalled = true;
        }

        @Override
        public void onNewPoll(Poll poll) {
            assertEquals(expectedPoll, poll);
            stage.onPollFinished();
            onNewPollCalled = true;
        }

        public boolean isOnStageFinishedCalled() {
            return onStageFinishedCalled;
        }

        public boolean isOnNewPollCalled() {
            return onNewPollCalled;
        }
    }

    @Test
    void testStartStage() {
        Poll expected = mock(Poll.class);
        PollCommand expectedCommand = mock(PollCommand.class);
        when(expected.getResultCommand()).thenReturn(expectedCommand);
        Optional<Poll> p1 = Optional.empty();
        Optional<Poll> p2 = Optional.of(expected);
        Optional<Poll> p3 = Optional.empty();
        Supplier<Optional<Poll>> s1 = () -> p1;
        Supplier<Optional<Poll>> s2 = () -> p2;
        Supplier<Optional<Poll>> s3 = () -> p3;
        Queue<Supplier<Optional<Poll>>> pollSupplierQueue = new LinkedList<>(List.of(s1, s2, s3));

        Stage stage = new Stage(StageType.Day, pollSupplierQueue);
        StageObserverMock observer = new StageObserverMock(stage, expected, List.of(expectedCommand));
        stage.addObserver(observer);
        stage.startStage();
        assertTrue(observer.isOnStageFinishedCalled());
        assertTrue(observer.isOnNewPollCalled());
    }
}
