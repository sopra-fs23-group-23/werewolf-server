package ch.uzh.ifi.hase.soprafs23.agora;

import ch.uzh.ifi.hase.soprafs23.constant.Reason;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import ch.uzh.ifi.hase.soprafs23.service.AgoraService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.repository.init.ResourceReader.Type.JSON;

public class AgoraTest {

    @Test
    void createRequestBody() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Player aPlayer = new Player((long) 10, "Willy");
        String expectedRequestBody = "{\"uid\":10,\"privileges\":[\"join_channel\"],\"reason\":1,\"appid\":\"348d6a205d75436e916896366c5e315c\",\"cname\":\"TestChannel\",\"time\":120}";

        Method privateMethod = Agora.class.getDeclaredMethod("createRequestBody", Optional.class, Optional.class, String.class, Reason.class);
        privateMethod.setAccessible(true);
        String result = (String) privateMethod.invoke(new AgoraService(), Optional.of(aPlayer), Optional.of("TestChannel"), "join_channel", Reason.KICK_VILLAGER);
        System.out.println(result);
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

        List<JsonNode> sampleRules = new ArrayList<>();
        sampleRules.add(createJsonNode(7510442816L, 1, 2));
        sampleRules.add(createJsonNode(7510371326L, 3, 1717));
        sampleRules.add(createJsonNode(7510367011L, 2, 1));


        Agora.createHttpRequest(HttpMethod.GET, "rules");
        Agora.deleteRules(Reason.KICK_VILLAGER, Optional.empty());
        Agora.deleteRules(Reason.KICK_ALL, Optional.empty());
        Agora.deleteRules(Reason.MUTE_DEAD, Optional.empty());
    }

    private JsonNode createJsonNode(long id, int reason, int uid) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("id", id);
        node.put("uid", uid);
        node.put("reason", reason);
        return node;
    }

    private JsonNode createJsonNode(List<JsonNode> nodes) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.putArray("rules").addAll(nodes);
        return node;
    }

    @Test
    void testKickVillager() throws IOException, InterruptedException {
        Player player = new Player((long) 123, "John");
        Agora.kickVillager(player);
        String expectedRequestBody = "{\"uid\":123,\"privileges\":[\"join_channel\"],\"reason\":1,\"appid\":\"348d6a205d75436e916896366c5e315c\",\"time_in_seconds\":10}";
        JsonNode jsonNode = Agora.createHttpRequest(HttpMethod.POST, expectedRequestBody);
        assertTrue(jsonNode.toString().contains("success"));
    }

    @Test
    void testKickAll() throws IOException, InterruptedException {
        Agora.kickAll("TestChannel");
        String expectedRequestBody = "{\"privileges\":[\"join_channel\"],\"reason\":2,\"appid\":\"348d6a205d75436e916896366c5e315c\",\"cname\":\"TestChannel\",\"time_in_seconds\":10}";
        JsonNode jsonNode = Agora.createHttpRequest(HttpMethod.POST, expectedRequestBody);
        assertTrue(jsonNode.toString().contains("success"));
    }

    @Test
    void testMuteDeadPlayer() throws IOException, InterruptedException {
        Player player = new Player((long) 123, "John");
        Agora.muteDeadPlayer(player);
        String expectedRequestBody = "{\"uid\":123,\"privileges\":[\"publish_audio\"],\"reason\":3,\"appid\":\"348d6a205d75436e916896366c5e315c\",\"time_in_seconds\":10}";
        JsonNode jsonNode = Agora.createHttpRequest(HttpMethod.POST, expectedRequestBody);
        assertTrue(jsonNode.toString().contains("success"));
    }
}
