package com.misfit.cloudapisdk.datamodel;



import com.example.demo.domain.sleeps.FitnessSleep;

import java.util.Date;
import java.util.List;

public class MisfitSleep extends FitnessSleep{
//    private String id;
    private boolean autoDetected;
//    private Date startTime;
//    private int duration;
//    private List<SleepObject> sleepDetails;

    public MisfitSleep(){
        super();
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
    public boolean isAutoDetected() {
        return autoDetected;
    }

    public void setAutoDetected(boolean autoDetected) {
        this.autoDetected = autoDetected;
    }
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
//    public List<SleepObject> getSleepDetails() {
//        return sleepDetails;
//    }
//
//    public void setSleepDetails(List<SleepObject> sleepDetails) {
//        this.sleepDetails = sleepDetails;
//    }
}
