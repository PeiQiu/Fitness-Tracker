package com.jawbone.upplatformsdk.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransferDateToDateWithoutTime {

    public static Date transferDateWithoutTime(Date date){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String temp = formatter.format(date);
        System.out.println("transfer date to String without time : " + temp);
        try {
            return formatter.parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
