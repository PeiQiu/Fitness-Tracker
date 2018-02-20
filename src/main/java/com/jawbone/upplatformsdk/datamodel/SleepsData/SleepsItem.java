package com.jawbone.upplatformsdk.datamodel.SleepsData;


import java.awt.geom.FlatteningPathIterator;

public class SleepsItem {
    private String xid;
    private String title;
    private Long sub_type;
    private Long time_created;
    private Long time_completed;
    private Long date;
    private SleepsItemsDetails details;

    public SleepsItem() {
    }

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getSub_type() {
        return sub_type;
    }

    public void setSub_type(Long sub_type) {
        this.sub_type = sub_type;
    }

    public Long getTime_created() {
        return time_created;
    }

    public void setTime_created(Long time_created) {
        this.time_created = time_created;
    }

    public Long getTime_completed() {
        return time_completed;
    }

    public void setTime_completed(Long time_completed) {
        this.time_completed = time_completed;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public SleepsItemsDetails getDetails() {
        return details;
    }

    public void setDetails(SleepsItemsDetails details) {
        this.details = details;
    }
}
