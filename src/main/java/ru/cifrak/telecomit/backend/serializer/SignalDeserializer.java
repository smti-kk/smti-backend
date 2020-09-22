package ru.cifrak.telecomit.backend.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import ru.cifrak.telecomit.backend.entities.Signal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignalDeserializer extends StdDeserializer<List<Signal>> {

    protected SignalDeserializer() {
        super(List.class);
    }

    @Override
    public List<Signal> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ArrayNode treeNode = p.getCodec().readTree(p);
        List<Signal> signals = new ArrayList<>();
        treeNode.forEach(n -> {
            int id = n.get("id").asInt();
            for (Signal signal : Signal.values()) {
                if (signal.getId() == id) {
                    signals.add(signal);
                    return;
                }
            }
            throw new IllegalStateException("unknown signal id: " + id);
        });
        return signals;
    }
}
