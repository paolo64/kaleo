package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.concurrent.Futures;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class KaleoController extends Controller {

    @Inject
    WSClient wsc;

    private static final ObjectMapper mapper = new ObjectMapper();

    final static String STATUS_ERROR = "STATUS_ERROR";
    final static long TIMEOUT = 2000;


    /**
     * Creates a CompletionStage from a URL
     * @param url the url to call
     * @param key the property name where to put the result of the call in JSON
     * @return a CompletionStage<JsonNode> representing the result of the call to the related URL
     */
    private CompletionStage<JsonNode> getStageFromUrl(String url, String key) {

        ObjectNode result = mapper.createObjectNode();
        return wsc.url(url)
                .setRequestTimeout(TIMEOUT)
                .get()
                .thenApply( wsResponse -> {
                    String uri = wsResponse.getUri().toString();
                    result.set(key, wsResponse.asJson());

                    if (wsResponse.getStatus() == 200) {
                        result.put(key, wsResponse.getBody());
                    }
                    else {
                        result.put(key, String.format("%s: %d [%s]", STATUS_ERROR, wsResponse.getStatus(), uri));
                    }
                    return (JsonNode) result;
                })
                .exceptionally(ex -> {
                    result.put(key, ex.getMessage());
                    return result;
                });
    }


    public CompletionStage<Result> getUrlData() {

        JsonNode root = request().body().asJson();
        List<CompletionStage<JsonNode>> stages = new ArrayList<>();

        Iterator<String> iterator = root.fieldNames();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = root.get(key).asText();
            stages.add(getStageFromUrl(value, key));
        }

        return Futures
                .sequence(stages)
                .thenApply(wsResponses -> {
                    ArrayNode arrayNode = mapper.createArrayNode();
                    wsResponses.stream().forEach(wsResponse -> arrayNode.add(wsResponse));
                    return ok(arrayNode);
                });
    }
}
