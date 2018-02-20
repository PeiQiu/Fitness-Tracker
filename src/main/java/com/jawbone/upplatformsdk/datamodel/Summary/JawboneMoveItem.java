package com.jawbone.upplatformsdk.datamodel.Summary;

public class JawboneMoveItem {
    private String xid;
    private String title;
    private String type;
    private Long time_created;
    private Long time_completed;
    private Long date;
    private JawboneMoveItemDetails details;

    public JawboneMoveItem(){

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public JawboneMoveItemDetails getDetails() {
        return details;
    }

    public void setDetails(JawboneMoveItemDetails details) {
        this.details = details;
    }
}
