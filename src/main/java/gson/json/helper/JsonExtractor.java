package gson.json.helper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gson.json.helper.collector.JsonArrayCollector;

import java.util.Objects;
import java.util.stream.StreamSupport;

public class JsonExtractor {


    // Private constructor is used since this is a utility class
    private JsonExtractor() {
    }

    private static final String DOT_SEPARATOR = ".";

    /**
     * @param input The JsonElement from which the value has to be extracted.
     * @param extractionKey The (.) dot seperated extraction key used to specify the extraction path.
     * @return the extracted JsonElement if found. returns the input if the extractionKey doesn't return
     * a JsonElement.
     */
    public static JsonElement extractJsonObject(JsonElement input, String extractionKey) {
        if (extractionKey.contains(DOT_SEPARATOR)) {
            String currentKey = extractionKey.substring(0, extractionKey.indexOf("."));
            if (input instanceof JsonArray) {
                return extractFromJsonArray(input.getAsJsonArray(), extractionKey);
            } else if (input instanceof JsonObject) {
                return extractJsonObject(input.getAsJsonObject().get(currentKey),
                        extractionKey.substring(extractionKey.indexOf(DOT_SEPARATOR) + 1));
            }
        } else if (!extractionKey.isEmpty() && Objects.nonNull(input)) {
            if (input.getAsJsonObject().get(extractionKey) instanceof JsonArray)
                return input.getAsJsonObject().get(extractionKey).getAsJsonArray();
            else
                return input.getAsJsonObject().get(extractionKey);
        }
        return input;
    }

    /**
     * @param inputArray The JsonArray from which a JsonArray of elements has to be extracted.
     * @param path The (.) dot seperated path from which the output is extracted.
     * @return The JsonArray if JsonElements in the path is found. returns input if not found.
     */
    public static JsonArray extractFromJsonArray(JsonArray inputArray, String path) {
        return StreamSupport.stream(inputArray.spliterator(), false).filter(Objects::nonNull)
                .map(json -> extractJsonObject(json.getAsJsonObject(), path)).collect(new JsonArrayCollector());
    }
}
