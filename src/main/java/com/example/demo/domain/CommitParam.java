package com.example.demo.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class CommitParam {
    private Date start;
    private Date end;
    private boolean detail;

    public CommitParam(){

    }

    public CommitParam(Builder builder){
        this.start = builder.start;
        this.end = builder.end;
        this.detail = builder.detail;
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


    public static class Builder {
        private Date start;
        private Date end;
        private boolean detail;

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

        public CommitParam build(){
            return new CommitParam(this);
        }
    }
}
