package com.jawbone.upplatformsdk.datamodel;


import com.example.demo.domain.sessions.FitnessSession;

import java.util.Date;

import static com.jawbone.upplatformsdk.utils.ActivityType.chooseType;

public class JawboneOutputActiveData extends FitnessSession {

    public JawboneOutputActiveData(){
        super();
    }

    public JawboneOutputActiveData(Builder builder){
        super();
        this.activityType = builder.activityType;
        this.id = builder.id;
        this.startTime = builder.startTime;
        this.calories = builder.calories;
        this.distance = builder.distance;
        this.steps = builder.steps;
        this.duration = builder.duration;
    }

    public static class Builder{
        private String id;
        private String activityType;
        private Date startTime;
        private Long duration;
        private Long steps;
        private Float calories;
        private Float distance;


        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder activityType(int typeNumber) {
            this.activityType = chooseType(typeNumber);
            return this;
        }

        public Builder startTime(Date startTime) {
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

        public Builder distance(Float distance) {
            this.distance = distance;
            return this;
        }
        public JawboneOutputActiveData build(){
            return new JawboneOutputActiveData(this);
        }
    }
}
