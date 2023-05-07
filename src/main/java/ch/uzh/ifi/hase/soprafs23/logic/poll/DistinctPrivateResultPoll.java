package ch.uzh.ifi.hase.soprafs23.logic.poll;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.instantpollcommand.PrivateInstantPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.DistinctRandomTiedPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class DistinctPrivateResultPoll extends PrivateResultPoll {
    private final int requiredSelections;
    private final DistinctRandomTiedPollDecider tiedPollDecider;

    public DistinctPrivateResultPoll(Class<? extends Role> role, String question, List<PrivateResultPollOption> pollOptions,
            PollParticipant pollParticipant, int durationSeconds, DistinctRandomTiedPollDecider tiedPollDecider) {
        super(role, question, pollOptions, List.of(pollParticipant), durationSeconds, tiedPollDecider);
        this.requiredSelections = pollParticipant.getRemainingVotes();
        this.tiedPollDecider = tiedPollDecider;
    }

    private List<PollOption> getSelectedPollOptions() {
        return getPollOptions().stream()
            .filter(pollOption -> pollOption.getSupportersAmount() > 0)
            .toList();
    }

    private List<PrivateInstantPollCommand> toPrivateInstantPollCommands(List<PollOption> pollOptions) {
        return pollOptions.stream()
            .map(PrivateResultPollOption.class::cast)
            .map(PrivateResultPollOption::getPollCommand)
            .map(PrivateInstantPollCommand.class::cast)
            .toList();
    }

    @Override
    public void finish() {
        List<PollOption> selectedPollOptions = getSelectedPollOptions();
        if (selectedPollOptions.size() < requiredSelections) {
            tiedPollDecider.executeTiePoll(this, selectedPollOptions, this::finish);
        } else {
            executeSelectedPrivateInstantPollCommands(toPrivateInstantPollCommands(selectedPollOptions));
            super.notifyObserversFinished();
        }
    }

    public void executeSelectedPrivateInstantPollCommands(List<PrivateInstantPollCommand> privateInstantPollCommands) {
        privateInstantPollCommands.stream()
            .forEach(privateInstantPollCommand -> {
                privateInstantPollCommand.execute_instantly();
                privateInstantPollCommand.getInformationOwner().addPrivatePollCommand(privateInstantPollCommand);
            });
    }

    @Override
    public void castVote(PollParticipant voter, PollOption pollOption) throws IllegalArgumentException {
        if (pollOption.getSupporters().contains(voter)) {
            throw new IllegalArgumentException("You already voted for this option");
        }
        super.castVote(voter, pollOption);
    }
    
}