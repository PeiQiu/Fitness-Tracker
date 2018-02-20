package com.misfit.cloudapisdk.datamodel;

import java.util.List;


public class MisfitGoals {
    private List<MisfitGoal> goals;

    public MisfitGoals(){

    }

    public List<MisfitGoal> getGoals() {
        return goals;
    }

    public void setGoals(List<MisfitGoal> goals) {
        this.goals = goals;
    }
}
