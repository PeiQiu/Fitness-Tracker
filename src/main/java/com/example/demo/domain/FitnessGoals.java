package com.example.demo.domain;

import com.jawbone.upplatformsdk.datamodel.JawboneGoals;
import com.misfit.cloudapisdk.datamodel.MisfitGoal;
import com.misfit.cloudapisdk.datamodel.MisfitGoals;

public class FitnessGoals {
    //Misfit Goal Data
    private float points;
    private int targetPoints;
    //jawbone Goal Data
    private int steps;
    private int sleep;
    private float body_weight;
    private int body_weight_intent;

    public FitnessGoals(MisfitGoals misfitGoals) {
        MisfitGoal misfitGoal = misfitGoals.getGoals().get(0);// get current Goal
        this.points = misfitGoal.getPoints();
        this.targetPoints = misfitGoal.getTargetPoints();
    }

    public FitnessGoals(JawboneGoals jawboneGoals){
        this.steps = jawboneGoals.getData().getMove_steps();
        this.sleep = jawboneGoals.getData().getSleep_total();
        this.body_weight = jawboneGoals.getData().getBody_weight();
        this.body_weight_intent = jawboneGoals.getData().getBody_weight_intent();
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public int getTargetPoints() {
        return targetPoints;
    }

    public void setTargetPoints(int targetPoints) {
        this.targetPoints = targetPoints;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getSleep() {
        return sleep;
    }

    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    public float getBody_weight() {
        return body_weight;
    }

    public void setBody_weight(float body_weight) {
        this.body_weight = body_weight;
    }

    public int getBody_weight_intent() {
        return body_weight_intent;
    }

    public void setBody_weight_intent(int body_weight_intent) {
        this.body_weight_intent = body_weight_intent;
    }
}
