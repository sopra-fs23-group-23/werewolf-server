package ch.uzh.ifi.hase.soprafs23.logic.poll;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PrivatePollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class PrivateResultPoll extends Poll {

    public PrivateResultPoll(Class<? extends Role> role, String question, List<PrivateResultPollOption> privatePollOptions,
            List<PollParticipant> pollParticipants, int durationSeconds, TiedPollDecider tiedPollDecider) {
        super(
            role,
            question,
            privatePollOptions.stream().map(pollOption -> (PollOption) pollOption).toList(),
            pollParticipants,
            durationSeconds,
            tiedPollDecider
        );
    }

    protected void executeAndAddToInformationOwner(PollCommand resultCommand) {
        PrivatePollCommand privateInstantPollCommand = (PrivatePollCommand) resultCommand;
        privateInstantPollCommand.execute();
        privateInstantPollCommand.getInformationOwner().addPrivatePollCommand(privateInstantPollCommand);
    }

    @Override
    public void finish() {
        finish(this::executeAndAddToInformationOwner);
    }
    
}
