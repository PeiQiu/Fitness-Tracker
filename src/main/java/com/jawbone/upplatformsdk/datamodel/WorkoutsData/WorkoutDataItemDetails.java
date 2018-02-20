package com.jawbone.upplatformsdk.datamodel.WorkoutsData;



public class WorkoutDataItemDetails {
    private Long steps;
    private Long time;
    private Float meters;
    private Float calories;
    private String tz;

    public WorkoutDataItemDetails(){

    }

    public Long getSteps() {
        return steps;
    }

    public void setSteps(Long steps) {
        this.steps = steps;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Float getMeters() {
        return meters;
    }

    public void setMeters(Float meters) {
        this.meters = meters;
    }

    public Float getCalories() {
        return calories;
    }

    public void setCalories(Float calories) {
        this.calories = calories;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }
}
