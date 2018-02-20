package com.example.demo.domain.Sharings;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class SharingActiveData {

    @Id
    private String id;

    private String activityType;
    private Long startTime;
    private Long duration;
    private Long steps;
    private Float calories;
    private Float distance;
    private Boolean type;

    public SharingActiveData(){

    }

    public SharingActiveData(Builder builder){
        this.activityType = builder.activityType;
        this.startTime = builder.startTime;
        this.distance = builder.distance;
        this.calories = builder.calories;
        this.duration = builder.duration;
        this.steps = builder.steps;
        this.type = builder.type;
    }


    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
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

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public static class Builder{
        private String activityType;
        private Long startTime;
        private Long duration;
        private Long steps;
        private Float calories;
        private Float distance;
        private Boolean type;

        public Builder type(boolean type){
            this.type = type;
            return this;
        }
        public Builder activityType(String activityType) {
            this.activityType = activityType;
            return this;
        }

        public Builder startTime(Long startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder duration(Long duration) {
            this.duration = duration;
            return this;
        }

        public Builder steps(Long steps) {
            this.steps = steps;
            return this;
        }

        public Builder calories(Float calories) {
            this.calories = calories;
            return this;
        }

        public Builder setDistance(Float distance) {
            this.distance = distance;
            return this;
        }

        public SharingActiveData build(){
            return new SharingActiveData(this);
        }
    }
}
