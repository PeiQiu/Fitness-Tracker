package com.jawbone.upplatformsdk.datamodel.SleepsData;


import com.jawbone.upplatformsdk.datamodel.Meta;
import com.jawbone.upplatformsdk.datamodel.SleepsData.SleepsResponseBodyData;

public class JawboneSleeps {
    private Meta meta;
    private SleepsResponseBodyData data;

    public JawboneSleeps() {
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public SleepsResponseBodyData getData() {
        return data;
    }

    public void setData(SleepsResponseBodyData data) {
        this.data = data;
    }
}
