package uk.ac.ed.inf;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

public class ListSerializer<T> extends JsonSerializer<List<T>> {

    @Override
    public void serialize(List<T> items, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        for (T item : items) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("item", item);
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
    }
}
