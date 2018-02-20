package com.jawbone.upplatformsdk.datamodel;


import com.jawbone.upplatformsdk.datamodel.HeartRateData.HeartRateResponseBody;

public class HeartRate {
    private Meta meta;
    private HeartRateResponseBody data;

    public HeartRate() {
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public HeartRateResponseBody getData() {
        return data;
    }

    public void setData(HeartRateResponseBody data) {
        this.data = data;
    }
}
