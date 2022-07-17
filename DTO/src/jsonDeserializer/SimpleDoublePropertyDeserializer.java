package jsonDeserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.lang.reflect.Type;

public class SimpleDoublePropertyDeserializer implements JsonDeserializer<SimpleDoubleProperty> {

    @Override
    public SimpleDoubleProperty deserialize(JsonElement json, Type srcType, JsonDeserializationContext context)
            throws JsonParseException {
        return new SimpleDoubleProperty(json.getAsJsonPrimitive().getAsDouble());
    }

}