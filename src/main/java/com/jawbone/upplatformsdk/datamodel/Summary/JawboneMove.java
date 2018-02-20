package com.jawbone.upplatformsdk.datamodel.Summary;


import com.jawbone.upplatformsdk.datamodel.Meta;

public class JawboneMove {
    private Meta meta;
    private JawboneMoveResponseBody data;

    public JawboneMove(){

    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public JawboneMoveResponseBody getData() {
        return data;
    }

    public void setData(JawboneMoveResponseBody data) {
        this.data = data;
    }
}
