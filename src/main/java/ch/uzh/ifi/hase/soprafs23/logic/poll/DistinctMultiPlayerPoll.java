package ch.uzh.ifi.hase.soprafs23.logic.poll;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;

public class DistinctMultiPlayerPoll extends PlayerPoll {

    public DistinctMultiPlayerPoll(List<PollOption> pollOptions, List<PollParticipant> pollParticipants,
            int durationSeconds, TiedPollDecider tiedPollDecider, boolean skippable) {
        super(pollOptions, pollParticipants, durationSeconds, tiedPollDecider, skippable);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void castVote(PollParticipant voter, PollOption pollOption) {
        // TODO Auto-generated method stub
        super.castVote(voter, pollOption);
    }
    
}