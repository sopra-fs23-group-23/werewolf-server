package ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;

public class DistinctRandomTiedPollDecider implements TiedPollDecider {
    /**
     * @pre Poll instanceof DistinctPrivateResultPoll
     */
    @Override
    public void executeTiePoll(Poll poll, List<PollOption> pollOptions, Runnable onTiePollFinished) {
        List<PollOption> unselected = poll.getPollOptions().stream().filter(pollOption -> !pollOptions.contains(pollOption)).collect(Collectors.toCollection(ArrayList::new));
        PollParticipant pollParticipant = poll.getPollParticipants().stream().findFirst().get();
        while (pollParticipant.getRemainingVotes() != 0) {
            PollOption randomUnselected = unselected.get(new Random().nextInt(unselected.size()));
            poll.castVote(pollParticipant, randomUnselected);
            unselected.remove(randomUnselected);
        }
        onTiePollFinished.run();
    }
    
}
