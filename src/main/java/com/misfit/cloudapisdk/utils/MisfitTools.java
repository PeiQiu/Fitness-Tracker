package com.misfit.cloudapisdk.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MisfitTools {

    public static String PatternMisfitTime = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static Date MisfitTimeToDate(String text){
        DateFormat df = new SimpleDateFormat(PatternMisfitTime){
            public Date parse(String source,ParsePosition pos) {
                return super.parse(source.replaceFirst(":(?=[0-9]{2}$)",""),pos);
            }
        };
        try {
            return df.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
