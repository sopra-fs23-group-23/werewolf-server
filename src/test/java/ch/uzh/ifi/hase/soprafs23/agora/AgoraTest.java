package ch.uzh.ifi.hase.soprafs23.agora;

import ch.uzh.ifi.hase.soprafs23.constant.Reason;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.service.AgoraService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

public class AgoraTest {

    private JsonNode createSampleKickVillagerRule() throws IOException, InterruptedException{
        Player player = new Player((long) 123, "John");
        Agora.kickVillager(player, "Testchannel");
        String expectedRequestBody = "{\"uid\":123,\"cname\":\"Testchannel\",\"privileges\":[\"join_channel\"],\"reason\":1,\"appid\":\"348d6a205d75436e916896366c5e315c\",\"time_in_seconds\":10}";
        return Agora.createHttpRequest(HttpMethod.POST, expectedRequestBody);
    }

    private JsonNode createSampleMuteDeadRule() throws IOException, InterruptedException{
        Player player = new Player((long) 1234, "Eggmann");
        Agora.muteDeadPlayer(player, "Testchannel");
        String expectedRequestBody = "{\"uid\":1234,\"cname\":\"Testchannel\",\"privileges\":[\"publish_audio\"],\"reason\":3,\"appid\":\"348d6a205d75436e916896366c5e315c\",\"time_in_seconds\":10}";
        return Agora.createHttpRequest(HttpMethod.POST, expectedRequestBody);
    }

    private List<JsonNode> createMultipleRules() throws IOException, InterruptedException{
        List<JsonNode> sampleRules = new ArrayList<>();
        sampleRules.add(createSampleMuteDeadRule());
        sampleRules.add(createSampleKickVillagerRule());
        return sampleRules;
    }
    @Test
    void createRequestBody() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Player aPlayer = new Player((long) 10, "Willy");
        String expectedRequestBody = "{\"uid\":10,\"privileges\":[\"join_channel\"],\"reason\":1,\"appid\":\"348d6a205d75436e916896366c5e315c\",\"cname\":\"Testchannel\",\"time\":120}";

        Method privateMethod = Agora.class.getDeclaredMethod("createRequestBody", Optional.class, Optional.class, String.class, Reason.class);
        privateMethod.setAccessible(true);
        String result = (String) privateMethod.invoke(new AgoraService(), Optional.of(aPlayer), Optional.of("Testchannel"), "join_channel", Reason.KICK_VILLAGER);
        assertEquals(expectedRequestBody, result);
    }

    @Test
    void createHttpRequest() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException, InterruptedException {
        Method privateMethod = Agora.class.getDeclaredMethod("createHttpRequest", HttpMethod.class, String.class);
        privateMethod.setAccessible(true);
        Object result = privateMethod.invoke(new Agora(), HttpMethod.GET, "");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResult = objectMapper.readTree(result.toString());
        String status = jsonResult.get("status").asText();
        assertEquals("success", status);
    }

    @Test
    void testDeleteRules() throws IOException, InterruptedException {
        createSampleMuteDeadRule();
        createSampleKickVillagerRule();

        Agora.deleteRules(Reason.KICK_VILLAGER, "Testchannel");
        Agora.deleteRules(Reason.MUTE_DEAD, "Testchannel");

        JsonNode jsonNode = Agora.getRules();
        List<JsonNode> rules = StreamSupport.stream(jsonNode.spliterator(), false)
                .filter(r -> r.get("cname").asText() == "Testchannel")
                .map(JsonNode.class::cast)
                .toList();

        assertTrue(rules.isEmpty());
    }
    @Test
    void testKickVillager() throws IOException, InterruptedException {
        Player player = new Player((long) 123, "John");
        Agora.kickVillager(player, "Testchannel");
        String expectedRequestBody = "{\"uid\":123,\"privileges\":[\"join_channel\"],\"reason\":1,\"appid\":\"348d6a205d75436e916896366c5e315c\",\"time_in_seconds\":10}";
        JsonNode jsonNode = Agora.createHttpRequest(HttpMethod.POST, expectedRequestBody);
        assertTrue(jsonNode.toString().contains("success"));
    }

    @Test
    void testDeleteAllRules() throws IOException, InterruptedException{
        createMultipleRules();
        Agora.deleteAllRules("Testchannel");
        JsonNode jsonNode = Agora.getRules();
        List<JsonNode> rules = StreamSupport.stream(jsonNode.spliterator(), false)
                .filter(r -> r.get("cname").asText() == "Testchannel")
                .map(JsonNode.class::cast)
                .toList();
        assertTrue(rules.isEmpty());
    }

    @Test
    void testKickAll() throws IOException, InterruptedException {
        Agora.kickAll("Testchannel");
        String expectedRequestBody = "{\"privileges\":[\"join_channel\"],\"reason\":2,\"appid\":\"348d6a205d75436e916896366c5e315c\",\"cname\":\"Testchannel\",\"time_in_seconds\":10}";
        JsonNode jsonNode = Agora.createHttpRequest(HttpMethod.POST, expectedRequestBody);
        assertTrue(jsonNode.toString().contains("success"));
    }

    @Test
    void testMuteDeadPlayer() throws IOException, InterruptedException {
        Player player = new Player((long) 123, "John");
        Agora.muteDeadPlayer(player, "Testchannel");
        String expectedRequestBody = "{\"uid\":123,\"privileges\":[\"publish_audio\"],\"reason\":3,\"appid\":\"348d6a205d75436e916896366c5e315c\",\"time_in_seconds\":10}";
        JsonNode jsonNode = Agora.createHttpRequest(HttpMethod.POST, expectedRequestBody);
        assertTrue(jsonNode.toString().contains("success"));
    }
}
