package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.Reason;
import ch.uzh.ifi.hase.soprafs23.logic.lobby.Player;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AgoraServiceTest {

    @Test
    void createRequestBody() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Player aPlayer = new Player((long) 10, "Willy");
        String expectedRequestBody = "{\"uid\":10,\"privileges\":[\"join_channel\"],\"reason\":1,\"appid\":\"348d6a205d75436e916896366c5e315c\",\"cname\":\"TestChannel\",\"time\":120}";

        Method privateMethod = AgoraService.class.getDeclaredMethod("createRequestBody", Optional.class, Optional.class, String.class, Reason.class);
        privateMethod.setAccessible(true);
        String result = (String) privateMethod.invoke(new AgoraService(), Optional.of(aPlayer), Optional.of("TestChannel"), "join_channel", Reason.KICK_VILLAGER);
        System.out.println(result);
        assertEquals(expectedRequestBody, result);
    }

    @Test
    void createHttpRequest() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException, InterruptedException {
        Method privateMethod = AgoraService.class.getDeclaredMethod("createHttpRequest", HttpMethod.class, String.class);
        privateMethod.setAccessible(true);
        Object result = privateMethod.invoke(new AgoraService(), HttpMethod.GET, "");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResult = objectMapper.readTree(result.toString());
        String status = jsonResult.get("status").asText();
        assertEquals("success", status);
    }

    //TODO: hasn't yet worked to make a nice Test, originally wanted to sort for just one rule and then check if function made single call to delete that rule.
    @Test
    void testDeleteRules() throws IOException, InterruptedException {

        List<JsonNode> sampleRules = new ArrayList<>();
        sampleRules.add(createJsonNode(7510442816L, 1, 2));
        sampleRules.add(createJsonNode(7510371326L, 3, 1818));
        sampleRules.add(createJsonNode(7510367011L, 2, 1));

        AgoraService agoraServiceMock = mock(AgoraService.class);

        when(agoraServiceMock.createHttpRequest(eq(HttpMethod.GET), eq("")))
                .thenReturn(createJsonNode(sampleRules));

        AgoraService agoraService = new AgoraService();

        Player player = new Player((long) 1717, "Willy");
        agoraServiceMock.deleteRules(Reason.MUTE_DEAD, Optional.of(player));

        // Verify that the createHttpRequest method was called with the expected arguments
        verify(agoraServiceMock, never()).createHttpRequest(eq(HttpMethod.DELETE), any());
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
        AgoraService agoraServiceMock = mock(AgoraService.class);
        doCallRealMethod().when(agoraServiceMock).kickVillager(player);
        agoraServiceMock.kickVillager(player);

        String expectedRequestBody = "{\"uid\":123,\"privileges\":[\"join_channel\"],\"reason\":1,\"appid\":\"348d6a205d75436e916896366c5e315c\",\"time\":120}";
        verify(agoraServiceMock).createHttpRequest(HttpMethod.POST, expectedRequestBody);
    }

    @Test
    void testKickAll() throws IOException, InterruptedException {
        AgoraService agoraServiceMock = mock(AgoraService.class);
        doCallRealMethod().when(agoraServiceMock).kickAll("TestChannel");
        agoraServiceMock.kickAll("TestChannel");


        String expectedRequestBody = "{\"privileges\":[\"join_channel\"],\"reason\":2,\"appid\":\"348d6a205d75436e916896366c5e315c\",\"cname\":\"TestChannel\",\"time\":120}";
        verify(agoraServiceMock).createHttpRequest(HttpMethod.POST, expectedRequestBody);
    }

    @Test
    void testMuteDeadPlayer() throws IOException, InterruptedException {
        Player player = new Player((long) 123, "John");
        AgoraService agoraServiceMock = mock(AgoraService.class);
        doCallRealMethod().when(agoraServiceMock).muteDeadPlayer(player);
        agoraServiceMock.muteDeadPlayer(player);

        String expectedRequestBody = "{\"uid\":123,\"privileges\":[\"publish_audio\"],\"reason\":3,\"appid\":\"348d6a205d75436e916896366c5e315c\",\"time\":120}";
        verify(agoraServiceMock).createHttpRequest(HttpMethod.POST, expectedRequestBody);
    }

    @Test
    void testMuteTroll() throws IOException, InterruptedException {
        Player player = new Player((long) 123, "John");

        AgoraService agoraServiceMock = mock(AgoraService.class);
        doCallRealMethod().when(agoraServiceMock).muteTroll(player);
        agoraServiceMock.muteTroll(player);

        String expectedRequestBody = "{\"uid\":123,\"privileges\":[\"publish_audio\"],\"reason\":4,\"appid\":\"348d6a205d75436e916896366c5e315c\",\"time\":120}";
        verify(agoraServiceMock).createHttpRequest(HttpMethod.POST, expectedRequestBody);
    }


}
