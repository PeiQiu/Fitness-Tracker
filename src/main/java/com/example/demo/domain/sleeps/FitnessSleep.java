package com.example.demo.domain.sleeps;


import com.example.demo.domain.sleeps.FitnessSleepPhasesItem;

import java.util.Date;
import java.util.List;

public class FitnessSleep {
    protected String id;
    protected Date startTime;
    protected Long duration;
    protected List<FitnessSleepPhasesItem> sleepDetails;

    public FitnessSleep(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

//    public int getDuration() {
//        return duration;
//    }
//
//    public void setDuration(int duration) {
//        this.duration = duration;
//    }


    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public List<FitnessSleepPhasesItem> getSleepDetails() {
        return sleepDetails;
    }

    public void setSleepDetails(List<FitnessSleepPhasesItem> sleepDetails) {
        this.sleepDetails = sleepDetails;
    }
}
