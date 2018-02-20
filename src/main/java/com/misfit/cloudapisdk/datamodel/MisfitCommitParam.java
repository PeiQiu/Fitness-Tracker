package com.misfit.cloudapisdk.datamodel;


import java.util.Date;

public class MisfitCommitParam {
    private Date start;
    private Date end;
    private boolean detail;
    private String device;


    public MisfitCommitParam(){

    }

    public MisfitCommitParam(Builder builder){
        this.start = builder.start;
        this.end = builder.end;
        this.detail = builder.detail;
        this.device = builder.device;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public boolean isDetail() {
        return detail;
    }

    public void setDetail(boolean detail) {
        this.detail = detail;
    }

    public static class Builder{
        private Date start;
        private Date end;
        private boolean detail;
        private String device;

        public Builder(){

        }

        public Builder device(String device){
            this.device = device;
            return this;
        }

        public Builder start(Date start) {
            this.start = start;
            return this;
        }

        public Builder end(Date end) {
            this.end = end;
            return this;
        }

        public Builder detail(boolean detail) {
            this.detail = detail;
            return this;
        }

        public MisfitCommitParam build(){
            return new MisfitCommitParam(this);
        }
    }
}
