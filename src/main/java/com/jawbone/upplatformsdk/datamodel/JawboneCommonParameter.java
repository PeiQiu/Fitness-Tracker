package com.jawbone.upplatformsdk.datamodel;


import com.example.demo.domain.CommitParam;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.jawbone.upplatformsdk.utils.TransferDateToDateWithoutTime.transferDateWithoutTime;

public class JawboneCommonParameter {
    private Date start;
    private Date end;
    private String xid;
    private boolean details;

    public JawboneCommonParameter(){

    }


    public JawboneCommonParameter(Builder builder){
        this.start = builder.start;
        this.end = builder.end;
        this.xid = builder.xid;
        this.details = builder.details;
    }

    public boolean isDetails() {
        return details;
    }

    public void setDetails(boolean details) {
        this.details = details;
    }

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
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

    public static class Builder{
        private  String xid;
        private Date start;
        private Date end;
        private boolean details;

        public Builder Start(Date start) {
            this.start = transferDateWithoutTime(start);
            return this;
        }

        public Builder Details(boolean details){
            this.details = details;
            return this;
        }

        public Builder Xid(String xd){
            this.xid = xid;
            return this;
        }

        public Builder End(Date end) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(end);
            gregorianCalendar.add(Calendar.DATE, 1);
            end = gregorianCalendar.getTime();
            this.end = transferDateWithoutTime(end);
            return this;
        }

        public JawboneCommonParameter build(){
            return new JawboneCommonParameter(this);
        }
    }
}
