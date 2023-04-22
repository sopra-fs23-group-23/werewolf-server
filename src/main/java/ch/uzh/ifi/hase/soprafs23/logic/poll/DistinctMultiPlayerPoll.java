package ch.uzh.ifi.hase.soprafs23.logic.poll;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class DistinctMultiPlayerPoll extends Poll {

    public DistinctMultiPlayerPoll(Class<? extends Role> role, String question, List<PollOption> pollOptions,
            List<PollParticipant> pollParticipants, int durationSeconds, TiedPollDecider tiedPollDecider) {
        super(role, question, pollOptions, pollParticipants, durationSeconds, tiedPollDecider);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void castVote(PollParticipant voter, PollOption pollOption) {
        // TODO Auto-generated method stub
        super.castVote(voter, pollOption);
    }
    
}
