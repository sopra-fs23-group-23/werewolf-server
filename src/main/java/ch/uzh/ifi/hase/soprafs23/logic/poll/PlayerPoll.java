package ch.uzh.ifi.hase.soprafs23.logic.poll;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.NullPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;

public class PlayerPoll extends Poll{
    private String question;
    private List<PollOption> pollOptions;
    private List<PollParticipant> pollParticipants;
    private int durationSeconds;
    private TiedPollDecider tiedPollDecider;
    private Optional<PollCommand> resultCommand = Optional.empty();

    public PlayerPoll(String question, List<PollOption> pollOptions, List<PollParticipant> pollParticipants, int durationSeconds,
            TiedPollDecider tiedPollDecider) {
        this.question = question;
        this.pollOptions = pollOptions;
        this.pollParticipants = pollParticipants;
        this.durationSeconds = durationSeconds;
        this.tiedPollDecider = tiedPollDecider;
    }

    @Override
    public void castVote(PollParticipant voter, PollOption pollOption) throws IllegalArgumentException{
        if (voter.getRemainingVotes() > 0) {
            pollOption.addSupporter(voter);
            voter.decreaseRemainingVotes();
        } else {
            throw new IllegalArgumentException("Voter has no remaining votes");
        }
    }

    public void removeVote(PollParticipant voter, PollOption pollOption) throws IllegalArgumentException{
        if (!pollOption.getSupporters().contains(voter)) {
            throw new IllegalArgumentException("Voter has not voted for this poll option.");
        }
        pollOption.removeSupporter(voter);
        voter.increaseRemainingVotes();
    }

    public void setResultCommand(PollCommand resultCommand) {
        this.resultCommand = Optional.of(resultCommand);
    }

    private List<PollOption> getTiedPollOptions(int supportersAmount, List<PollOption> pollOptionsOrderedBySupporters) {
        return pollOptionsOrderedBySupporters.stream().filter(pollOption -> pollOption.getSupportersAmount() == supportersAmount).toList();
    }

    @Override
    public void finish() {
        List<PollOption> pollOptionsOrderedBySupporters = pollOptions.stream().sorted(Comparator.comparing(PollOption::getSupportersAmount).reversed()).toList();
        PollOption first = pollOptionsOrderedBySupporters.get(0);
        PollOption second = pollOptionsOrderedBySupporters.get(1);
        boolean tie = first.getSupportersAmount() == second.getSupportersAmount();
        if (!tie) {
            setResultCommand(first.getPollCommand());
            super.finish();
        } else {
            tiedPollDecider.executeTiePoll(this, getTiedPollOptions(first.getSupportersAmount(), pollOptionsOrderedBySupporters), super::finish);
        }
    }

    @Override
    public PollCommand getResultCommand() {
        if (resultCommand.isPresent()) {
            return resultCommand.get();
        } else {
            return new NullPollCommand();
        }
    }

    @Override
    public int getDurationSeconds() {
        return durationSeconds;
    }

    @Override
    public Collection<PollParticipant> getPollParticipants() {
        return this.pollParticipants;
    }

    @Override
    public Collection<PollOption> getPollOptions() {
        return pollOptions;
    }

    @Override
    public String getQuestion() {
        return question;
    }
    
}
