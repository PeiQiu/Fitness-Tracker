package com.jawbone.upplatformsdk.datamodel.Summary;


public class JawboneMoveItemDetails {
    private Float distance;
    private Long steps;
    private Long active_time;
    private Long longest_active;
    private Float calories;
    private Float wo_calories;
    private String tz;

    public JawboneMoveItemDetails(){

    }

    public Float getWo_calories() {
        return wo_calories;
    }

    public void setWo_calories(Float wo_calories) {
        this.wo_calories = wo_calories;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Long getSteps() {
        return steps;
    }

    public void setSteps(Long steps) {
        this.steps = steps;
    }

    public Long getActive_time() {
        return active_time;
    }

    public void setActive_time(Long active_time) {
        this.active_time = active_time;
    }

    public Long getLongest_active() {
        return longest_active;
    }

    public void setLongest_active(Long longest_active) {
        this.longest_active = longest_active;
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
