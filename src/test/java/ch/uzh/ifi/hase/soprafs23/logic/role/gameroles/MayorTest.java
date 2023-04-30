package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;

import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;

public class MayorTest {

    @Test
    void testExecuteTiePoll_noMayor() {
        TiedPollDecider tiedPollDecider = mock(TiedPollDecider.class);
        Mayor mayor = new Mayor(null, tiedPollDecider);

        Poll poll = mock(Poll.class);

        mayor.executeTiePoll(poll, List.of(), poll::finish);

    }
}
