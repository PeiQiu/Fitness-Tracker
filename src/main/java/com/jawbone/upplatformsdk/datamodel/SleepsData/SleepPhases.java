package com.jawbone.upplatformsdk.datamodel.SleepsData;


import com.jawbone.upplatformsdk.datamodel.Meta;

public class SleepPhases {
    private Meta meta;
    private SleepPhasesResponseBody data;

    public SleepPhases(){

    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public SleepPhasesResponseBody getData() {
        return data;
    }

    public void setData(SleepPhasesResponseBody data) {
        this.data = data;
    }
}
