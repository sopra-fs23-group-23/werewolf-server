package ch.uzh.ifi.hase.soprafs23.logic.poll;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;

public class PlayerPoll extends Poll implements PollObserver{
    private List<PollOption> pollOptions;
    private List<PollParticipant> pollParticipants;
    private int durationSeconds;
    private TiedPollDecider tiedPollDecider;
    private boolean skippable;

    public PlayerPoll(List<PollOption> pollOptions, List<PollParticipant> pollParticipants, int durationSeconds,
            TiedPollDecider tiedPollDecider, boolean skippable) {
        this.pollOptions = pollOptions;
        this.pollParticipants = pollParticipants;
        this.durationSeconds = durationSeconds;
        this.tiedPollDecider = tiedPollDecider;
        this.skippable = skippable;
    }

    public void castVote(PollParticipant voter, PollOption pollOption) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'castVote'");
    }

    public void skipVote(PollParticipant voter) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'skipVote'");
    }

    @Override
    public void startPoll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startPoll'");
    }

    @Override
    public PollCommand getResultCommand() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getResultCommand'");
    }

    @Override
    public int getDurationSeconds() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDurationSeconds'");
    }

    @Override
    public void onPollFinished() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onPollFinished'");
    }
    
}
