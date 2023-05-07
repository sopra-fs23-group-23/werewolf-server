package ch.uzh.ifi.hase.soprafs23.logic.poll;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.NullPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class Poll{
    private Class<? extends Role> role;
    private String question;
    private List<PollOption> pollOptions;
    private List<PollParticipant> pollParticipants;
    private int durationSeconds;
    private Date scheduledFinish;
    private TiedPollDecider tiedPollDecider;
    private Optional<PollCommand> resultCommand = Optional.empty();
    private List<PollObserver> observers = new ArrayList<>();
    
    public void addObserver(PollObserver observer) {
        observers.add(observer);
    }

    protected void notifyObserversFinished() {
        observers.stream().forEach(o->o.onPollFinished());
    }

    public Poll(Class<? extends Role> role, String question, List<PollOption> pollOptions, List<PollParticipant> pollParticipants, int durationSeconds,
            TiedPollDecider tiedPollDecider) {
        this.role = role;
        this.question = question;
        this.pollOptions = pollOptions;
        this.pollParticipants = pollParticipants;
        this.durationSeconds = durationSeconds;
        this.tiedPollDecider = tiedPollDecider;
    }

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

    public void finish() {
        finish(this::setResultCommand);
    }

    protected void finish(Consumer<PollCommand> resultCommandConsumer) {
        List<PollOption> pollOptionsOrderedBySupporters = pollOptions.stream().sorted(Comparator.comparing(PollOption::getSupportersAmount).reversed()).toList();
        PollOption first = pollOptionsOrderedBySupporters.get(0);
        PollOption second = pollOptionsOrderedBySupporters.get(1);
        boolean tie = first.getSupportersAmount() == second.getSupportersAmount();
        if (!tie) {
            resultCommandConsumer.accept(first.getPollCommand());
            notifyObserversFinished();
        } else {
            tiedPollDecider.executeTiePoll(this, getTiedPollOptions(first.getSupportersAmount(), pollOptionsOrderedBySupporters), this::notifyObserversFinished);
        }
    }

    public PollCommand getResultCommand() {
        if (resultCommand.isPresent()) {
            return resultCommand.get();
        } else {
            return new NullPollCommand();
        }
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public Date getScheduledFinish() {
        return scheduledFinish;
    }

    public Date calculateScheduledFinish(Calendar calendar) {
        calendar.add(Calendar.SECOND, getDurationSeconds());
        return calendar.getTime();
    }

    public void setScheduledFinish(Date scheduledFinish) {
        this.scheduledFinish = scheduledFinish;
    }

    public void setPollOptions(List<PollOption> pollOptions) {
        this.pollOptions = pollOptions;
    }

    public void setPollParticipants(List<PollParticipant> pollParticipants) {
        this.pollParticipants = pollParticipants;
    }

    public void setTiedPollDecider(TiedPollDecider tiedPollDecider) {
        this.tiedPollDecider = tiedPollDecider;
    }

    public void setRole(Class<? extends Role> role) {
        this.role = role;
    }

    public Collection<PollParticipant> getPollParticipants() {
        return this.pollParticipants;
    }

    public Collection<PollOption> getPollOptions() {
        return pollOptions;
    }

    public String getQuestion() {
        return question;
    }

    public Class<? extends Role> getRole() {
        return role;
    }
    
}
