package com.example.demo.domain;

import java.util.List;

public class FitnessSummarys {
    protected List<FitnessSummary> summary;

    public FitnessSummarys(){

    }

    public List<FitnessSummary> getSummary() {
        return summary;
    }

    public void setSummary(List<FitnessSummary> summary) {
        this.summary = summary;
    }
}
