package gson.json.helper.collector;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * A custom collector to collect any stream of JsonElements into a JsonArray container.
 */
public class JsonArrayCollector implements Collector<JsonElement, List<JsonElement>, JsonArray> {
    /**
     * @return Supplier that supplies a synchronized ArrayList.
     */
    @Override
    public Supplier<List<JsonElement>> supplier() {
        return () -> Collections.synchronizedList(new ArrayList<JsonElement>());
    }

    /**
     * @return BiConsumer that accumulates incoming stream elements into an intermediate ArrayList.
     */
    @Override
    public BiConsumer<List<JsonElement>, JsonElement> accumulator() {
        return (arr, element) -> Optional.ofNullable(element)
                .ifPresent(arr::add);
    }

    /**
     * @return BinaryOperator That combines two ArrayLists.
     */
    @Override
    public BinaryOperator<List<JsonElement>> combiner() {
        return (primary, secondary) -> {
            if (Objects.nonNull(secondary) && !secondary.isEmpty()) {
                primary.addAll(secondary);
            }
            return primary;
        };
    }

    /**
     * @return Function That converts the ArrayList into a JsonArray.
     */
    @Override
    public Function<List<JsonElement>, JsonArray> finisher() {
        return combinedList -> {
            JsonArray collectedJsonArray = new JsonArray();
            for (JsonElement element : combinedList) {
                collectedJsonArray.add(element);
            }
            return collectedJsonArray;
        };
    }

    /**
     * @return Set of Characteristics of this Collector. This collector is concurrent.
     */
    @Override
    public Set<Characteristics> characteristics() {
        Set<Characteristics> characteristics = new HashSet<>();
        characteristics.add(Characteristics.CONCURRENT);
        return characteristics;
    }
}
