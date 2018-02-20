package com.jawbone.upplatformsdk.datamodel.WorkoutsData;


import com.jawbone.upplatformsdk.datamodel.Meta;

public class WorkoutDatas {
    private Meta meta;
    private WorkoutsResponseBody data;

    public WorkoutDatas(){

    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public WorkoutsResponseBody getData() {
        return data;
    }

    public void setData(WorkoutsResponseBody data) {
        this.data = data;
    }
}
