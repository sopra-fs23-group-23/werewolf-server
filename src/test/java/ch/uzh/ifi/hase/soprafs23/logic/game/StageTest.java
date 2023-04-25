package ch.uzh.ifi.hase.soprafs23.logic.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        stage.addObserver(new StageObserver() {
            @Override
            public void onStageFinished() {
                assertThat("List equality without order", stage.getPollCommands(), containsInAnyOrder(List.of(expectedCommand).toArray()));
            }

            @Override
            public void onNewPoll(Poll poll) {
                assertEquals(expected, poll);
                stage.onPollFinished();
            }
        });
        stage.startStage();
    }
}
