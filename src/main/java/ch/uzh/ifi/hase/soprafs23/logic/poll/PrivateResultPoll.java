package ch.uzh.ifi.hase.soprafs23.logic.poll;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.NullPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.instantpollcommand.PrivateInstantPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.tiedpolldecider.TiedPollDecider;
import ch.uzh.ifi.hase.soprafs23.logic.role.Role;

public class PrivateResultPoll extends Poll {

    public PrivateResultPoll(Class<? extends Role> role, String question, List<PrivatePollOption> privatePollOptions,
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

    @Override
    public PollCommand getResultCommand() {
        PrivateInstantPollCommand resultCommand = (PrivateInstantPollCommand) super.getResultCommand();
        //resultCommand.getInformationOwner().addPrivatePollCommand(resultCommand);
        return new NullPollCommand();
    }
    
}
