package ch.uzh.ifi.hase.soprafs23.logic.poll;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class DistinctPrivateResultPoll extends PrivateResultPoll {

    public DistinctPrivateResultPoll(Class<? extends Role> role, String question, List<PrivatePollOption> pollOptions,
            List<PollParticipant> pollParticipants, int durationSeconds, TiedPollDecider tiedPollDecider) {
        super(role, question, pollOptions, pollParticipants, durationSeconds, tiedPollDecider);
    }

    @Override
    public void castVote(PollParticipant voter, PollOption pollOption) throws IllegalArgumentException {
        // TODO
        super.castVote(voter, pollOption);
    }
    
}