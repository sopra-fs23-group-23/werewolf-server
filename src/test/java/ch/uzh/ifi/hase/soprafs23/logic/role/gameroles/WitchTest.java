package ch.uzh.ifi.hase.soprafs23.logic.role.gameroles;


import ch.uzh.ifi.hase.soprafs23.logic.game.Game;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.logic.poll.Poll;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.KillPlayerPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.PollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.RemoveCommandPollCommand;
import ch.uzh.ifi.hase.soprafs23.logic.poll.pollcommand.WitchKillPlayerPollCommand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class WitchTest {

    private Witch witch;

    @BeforeEach
    public void setUp(){
        Game game = mock(Game.class);
        Supplier<List<Player>> mockAlivePlayerGetter = createMockAlivePlayersGetter(getAlivePlayers());
        Supplier<List<PollCommand>> mockCurrentStageCommands = this::mockPollCommandsList;
        Consumer<PollCommand> mockConsumerPollCommand = game::removePollCommandFromCurrentStage;
        witch = new Witch(mockAlivePlayerGetter, mockCurrentStageCommands, mockConsumerPollCommand);
        Player player = createMockPlayer();
        witch.addPlayer(player);
    }

    private Supplier<List<Player>> createMockAlivePlayersGetter (List<Player> expected) {
        return new Supplier<List<Player>>() {
            @Override
            public List<Player> get() {
                return expected.stream().filter(Player::isAlive).toList();
            }

        };
    }
    private Player createMockPlayer() {
        Player player = mock(Player.class);
        when(player.isAlive()).thenReturn(true);
        return player;
    }

    private List<Player> getAlivePlayers() {
        return List.of(
                createMockPlayer(),
                createMockPlayer(),
                createMockPlayer(),
                createMockPlayer()
        );
    }

    private List<PollCommand> mockPollCommandsList(){
        List<PollCommand> mockPollCommandsList = new ArrayList<>();
        mockPollCommandsList.add(new KillPlayerPollCommand(createMockPlayer()));
        return mockPollCommandsList;
    }


    @Test
    void testCreateNightPoll_Empty(){
        Witch newWitch = new Witch(null,this::mockPollCommandsList,null);
        Player player = createMockPlayer();
        newWitch.addPlayer(player);
        when(player.isAlive()).thenReturn(false);
        Optional<Poll> poll = newWitch.createNightPoll();
        assertEquals(Optional.empty(), poll);
    }

    @Test
    void testCreateNightPoll_notEmpty(){
        Optional<Poll> poll = witch.createNightPoll();
        assertEquals(1, poll.get().getPollParticipants().size());
        assertEquals(1, poll.get().getPollOptions().size());
        assertTrue(poll.get().getPollOptions().stream().findFirst().get().getPollCommand() instanceof RemoveCommandPollCommand);
    }

    @Test
    void testCreateSecondNightPoll_Empty(){
        Witch newWitch = new Witch(null,this::mockPollCommandsList,null);
        Player player = createMockPlayer();
        newWitch.addPlayer(player);
        when(player.isAlive()).thenReturn(false);
        Optional<Poll> poll = newWitch.createSecondNightPoll();
        assertEquals(Optional.empty(), poll);
    }

    @Test
    void testCreateSecondNightPoll_notEmpty(){
        Optional<Poll> poll = witch.createSecondNightPoll();
        assertEquals(1, poll.get().getPollParticipants().size());
        assertTrue(poll.get().getPollOptions().stream().findFirst().get().getPollCommand() instanceof WitchKillPlayerPollCommand);
    }
}
