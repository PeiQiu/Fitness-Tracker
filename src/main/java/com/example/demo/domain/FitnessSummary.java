package com.example.demo.domain;


import java.util.Date;

public class FitnessSummary {
    protected Date date;
    protected Long steps;
    protected Float calories;
    protected Float activityCalories;
    protected Float distance;

    public FitnessSummary(){

    }



    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getSteps() {
        return steps;
    }

    public void setSteps(Long steps) {
        this.steps = steps;
    }

    public Float getCalories() {
        return calories;
    }

    public void setCalories(Float calories) {
        this.calories = calories;
    }

    public Float getActivityCalories() {
        return activityCalories;
    }

    public void setActivityCalories(Float activityCalories) {
        this.activityCalories = activityCalories;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }
}
