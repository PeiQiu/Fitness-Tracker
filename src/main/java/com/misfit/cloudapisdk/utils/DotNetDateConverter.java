package com.misfit.cloudapisdk.utils;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DotNetDateConverter implements JsonDeserializer<Date> {

    public static String PatternMisfitTime = "yyyy-MM-dd'T'HH:mm:ssZ";

    @Override
    public Date deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String s = json.getAsJsonPrimitive().getAsString();
        DateFormat format = new SimpleDateFormat(PatternMisfitTime){
            public Date parse(String source,ParsePosition pos) {
                return super.parse(source.replaceFirst(":(?=[0-9]{2}$)",""),pos);
            }
        };

        try {
            return format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
