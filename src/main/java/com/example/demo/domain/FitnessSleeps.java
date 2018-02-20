package com.example.demo.domain;


import com.example.demo.domain.sleeps.FitnessSleep;

import java.util.List;

public class FitnessSleeps {
    protected List<FitnessSleep> sleeps;

    public FitnessSleeps(){

    }


    public List<FitnessSleep> getSleeps() {
        return sleeps;
    }

    public void setSleeps(List<FitnessSleep> sleeps) {
        this.sleeps = sleeps;
    }
}
