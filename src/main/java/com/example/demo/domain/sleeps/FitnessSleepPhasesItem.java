package com.example.demo.domain.sleeps;


import java.util.Date;

public class FitnessSleepPhasesItem {
    protected Date datetime;
    protected int value;

    public FitnessSleepPhasesItem(){

    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
