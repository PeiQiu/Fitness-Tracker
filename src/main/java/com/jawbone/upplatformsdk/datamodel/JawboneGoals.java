package com.jawbone.upplatformsdk.datamodel;

import com.jawbone.upplatformsdk.datamodel.GoalsData.GoalsResponseBody;

public class JawboneGoals {

    private Meta meta;
    private GoalsResponseBody data;

    public JawboneGoals() {
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public GoalsResponseBody getData() {
        return data;
    }

    public void setData(GoalsResponseBody data) {
        this.data = data;
    }
}
