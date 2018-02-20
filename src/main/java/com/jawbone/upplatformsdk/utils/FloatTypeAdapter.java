package com.jawbone.upplatformsdk.utils;


import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

public class FloatTypeAdapter implements JsonDeserializer<Float> {


    @Override
    public Float deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        System.out.println("element " + jsonElement.toString() );
        Float s;
        try {
            s = jsonElement.getAsFloat();
        }catch (NumberFormatException e){
            s = (float)0.0;
        }
        return s;
    }
}
