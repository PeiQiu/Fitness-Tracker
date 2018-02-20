package com.example.demo.tools;


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
import java.util.TimeZone;

public class DateDeserializer implements JsonDeserializer<Date> {

    public static String PatternMisfitTime = "yyyy-MM-dd'T'HH:mm:ssZ";

    @Override
    public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        System.out.println("JsonElement" + element.toString());
        String text = element.getAsString();
        System.out.println("initial json date :   :: " + text);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        DateFormat format = new SimpleDateFormat(PatternMisfitTime){
            public Date parse(String source,ParsePosition pos) {
                return super.parse(source.replaceFirst(":(?=[0-9]{2}$)",""),pos);
            }
        };

        try {
            return format.parse(text);
        } catch (ParseException exp) {
            System.err.println("Failed to parse Date: "+ exp);
            return null;
        }
    }
}
