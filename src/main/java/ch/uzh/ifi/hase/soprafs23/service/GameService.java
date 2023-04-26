package ch.uzh.ifi.hase.soprafs23.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import ch.uzh.ifi.hase.soprafs23.rest.dto.FractionGetDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Lobby;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollOption;
import ch.uzh.ifi.hase.soprafs23.logic.poll.PollParticipant;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PollGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.logicmapper.LogicDTOMapper;

@Service
@Transactional
public class GameService{
    private Map<Long, Game> games = new HashMap<>();

    /**
     * @pre lobby.getLobbySize() <= Lobby.MAX_SIZE && lobby.getLobbySize() >= Lobby.MIN_SIZE && lobby roles assigned
     * @param lobby
     */
    public Game createNewGame(Lobby lobby) {
        assert lobby.getLobbySize() <= Lobby.MAX_SIZE && lobby.getLobbySize() >= Lobby.MIN_SIZE;
        Game game = new Game(lobby);
        games.put(lobby.getId(), game);
        return game;
    }

    public Game getGame(Lobby lobby) {
        if (!games.containsKey(lobby.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No game found for lobby with id %d", lobby.getId()));
        }
        return games.get(lobby.getId());
    }

    public GameGetDTO toGameGetDTO(Game game) {
        return LogicDTOMapper.convertGameToGameGetDTO(game);
    }

    public void startGame(Game game) {
        game.startGame();
    }

    public void schedule(Runnable command, int delaySeconds) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(command, delaySeconds, TimeUnit.SECONDS);
    }

    public Poll getCurrentPoll(Game game) {
        try {
            return game.getCurrentPoll();
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    public boolean isPollParticipant(Poll poll, User user) {
        return poll.getPollParticipants().stream().anyMatch(p->p.getPlayer().getId() == user.getId());
    }

    public void validateParticipant(Poll poll, User user) {
        if (!isPollParticipant(poll, user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not participant in this poll.");
        }
    }

    public PollGetDTO toPollGetDTO(Poll poll) {
        return LogicDTOMapper.convertPollToPollGetDTO(poll);
    }

    public PollGetDTO censorPollGetDTO (PollGetDTO pollGetDTO) {
        pollGetDTO.setParticipants(Collections.emptyList());
        pollGetDTO.setPollOptions(Collections.emptyList());
        return pollGetDTO;
    }

    /**
     * @pre validateParticipant
     * @param poll
     * @param user
     * @return
     */
    public PollParticipant getParticipant (Poll poll, User user) {
        return poll.getPollParticipants().stream().filter(p->p.getPlayer().getId() == user.getId()).findFirst().get();
    }

    public PollOption getPollOption(Poll poll, Long pollOptionId) {
        Predicate<? super PollOption> optionFilter = (p->p.getPlayer().getId() == pollOptionId);
        if (!poll.getPollOptions().stream().anyMatch(optionFilter)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Selected option is not a valid option for this poll.");
        }
        return poll.getPollOptions().stream().filter(optionFilter).findFirst().get();
    }

    public void castVote(Poll poll, PollParticipant participant, PollOption option) {
        try {
            poll.castVote(participant, option);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    public void removeVote(Poll poll, PollParticipant participant, PollOption option) {
        try {
            poll.removeVote(participant, option);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    /**
     * @pre game is finished
     * @param game
     */
    public FractionGetDTO getFractionGetDTO(Game game) {
        assert game.isFinished();
        return LogicDTOMapper.convertFractionToFractionGetDTO(game.getWinner());
    }
}
