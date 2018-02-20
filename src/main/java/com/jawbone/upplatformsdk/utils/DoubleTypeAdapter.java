package com.jawbone.upplatformsdk.utils;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class DoubleTypeAdapter implements JsonDeserializer<Double> {
    @Override
    public Double deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Double d;
        try {
            d = jsonElement.getAsDouble();
        }catch (NumberFormatException e){
            d = 0.0;
        }
        return d;
    }
}
