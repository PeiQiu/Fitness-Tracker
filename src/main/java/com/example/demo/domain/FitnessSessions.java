package com.example.demo.domain;

import com.example.demo.domain.sessions.FitnessSession;

import java.util.List;

public class FitnessSessions {
    List<FitnessSession> sessions;

    public FitnessSessions(){

    }

    public List<FitnessSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<FitnessSession> sessions) {
        this.sessions = sessions;
    }
}
