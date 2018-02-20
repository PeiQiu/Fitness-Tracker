package com.jawbone.upplatformsdk.datamodel.SleepsData;


import com.example.demo.domain.sleeps.FitnessSleep;
import com.example.demo.domain.sleeps.FitnessSleepPhasesItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JawboneSleepDataItemOutputFormat extends FitnessSleep {
//    private String id;
//    private Date startTime;
//    private int duration;
//    private List<SleepPhasesOutputFormat> sleepDetails;
//
    public JawboneSleepDataItemOutputFormat(){
        super();
    }

    public JawboneSleepDataItemOutputFormat(Builder builder){
        super();
        this.id = builder.id;
        this.startTime = builder.startTime;
        this.duration = builder.duration;
        this.sleepDetails = builder.sleepDetails;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public Date getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(Date startTime) {
//        this.startTime = startTime;
//    }
//
//    public int getDuration() {
//        return duration;
//    }
//
//    public void setDuration(int duration) {
//        this.duration = duration;
//    }
//
//    public List<SleepPhasesOutputFormat> getSleepDetails() {
//        return sleepDetails;
//    }

//    public void setSleepDetails(List<SleepPhasesItem> sleepDetails) {
//        this.sleepDetails.clear();
//        for(SleepPhasesItem sleepPhasesItem : sleepDetails){
//            SleepPhasesOutputFormat sleepPhasesOutputFormat = new SleepPhasesOutputFormat(sleepPhasesItem);
//            this.sleepDetails.add(sleepPhasesOutputFormat);
//        }
//    }

    public static class Builder {
        private String id;
        private Date startTime;
        private Long duration;
        //private List<SleepPhasesOutputFormat> sleepDetails;
        private List<FitnessSleepPhasesItem> sleepDetails;
        public Builder Id(String id) {
            this.id = id;
            return this;
        }

        public Builder StartTime(Date startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder Duration(Long duration) {
            System.out.println("--------------------------------  put the duration : " + duration);
            this.duration = duration;
            return this;
        }

        public Builder SleepDetails(List<SleepPhasesItem> sleepDetails) {
            List<FitnessSleepPhasesItem> list = new ArrayList<>();
            for(SleepPhasesItem sleepPhasesItem : sleepDetails){
                SleepPhasesOutputFormat sleepPhasesOutputFormat = new SleepPhasesOutputFormat(sleepPhasesItem);
                list.add(sleepPhasesOutputFormat);
            }
            this.sleepDetails = list;
            return this;
        }
        public JawboneSleepDataItemOutputFormat build(){
            return new JawboneSleepDataItemOutputFormat(this);
        }
    }
}
