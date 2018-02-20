package com.jawbone.upplatformsdk.datamodel;


import com.example.demo.domain.FitnessSummary;

import java.util.Date;

public class JawboneOutputSummaryData extends FitnessSummary{

    public JawboneOutputSummaryData(){
        super();
    }

    public JawboneOutputSummaryData(Builder builder){
        super();
        this.activityCalories = builder.activityCalories;
        this.calories = builder.calories;
        this.date = builder.date;
        this.steps = builder.steps;
        this.distance = builder.distance;
    }

    public static class Builder{
        private Date date;
        private Long steps;
        private Float calories;
        private Float activityCalories;
        private Float distance;

        public Builder date(Date date) {
            this.date = date;
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

        public Builder activityCalories(Float activityCalories) {
            this.activityCalories = activityCalories;
            return this;
        }

        public Builder distance(Float distance) {
            this.distance = distance;
            return this;
        }
        public JawboneOutputSummaryData build(){
            return new JawboneOutputSummaryData(this);
        }
    }
}
