package com.jawbone.upplatformsdk.datamodel.SleepsData;

import com.example.demo.domain.sleeps.FitnessSleepPhasesItem;

import java.util.Date;

public class SleepPhasesOutputFormat extends FitnessSleepPhasesItem {
//    private Date datetime;
//    private int value;

    public SleepPhasesOutputFormat(){
        super();
    }
    public SleepPhasesOutputFormat(SleepPhasesItem sleepPhasesItem){
        super();
        System.out.println(">>>>>>>>>>> sleep phases // : " + sleepPhasesItem.getTime() + " ::: mules 1000 > " + sleepPhasesItem.getTime()*1000 + " date time : " + new Date(sleepPhasesItem.getTime()*1000).toString());
        this.datetime = new Date(sleepPhasesItem.getTime()*1000);
        this.value = sleepPhasesItem.getDepth();
    }

//    public Date getDatetime() {
//        return datetime;
//    }
//
//    public void setDatetime(Date datetime) {
//        this.datetime = datetime;
//    }
//
//    public int getValue() {
//        return value;
//    }
//
//    public void setValue(int value) {
//        this.value = value;
//    }
}
