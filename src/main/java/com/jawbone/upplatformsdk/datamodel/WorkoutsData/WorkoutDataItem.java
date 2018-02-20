package com.jawbone.upplatformsdk.datamodel.WorkoutsData;


public class WorkoutDataItem {
    private String xid;
    private String title;
    private String type;
    private Integer sub_type;
    private Long time_created;
    private Long time_completed;
    private Integer date;
    private WorkoutDataItemDetails details;

    public WorkoutDataItem(){

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

    public Integer getSub_type() {
        return sub_type;
    }

    public void setSub_type(Integer sub_type) {
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

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public WorkoutDataItemDetails getDetails() {
        return details;
    }

    public void setDetails(WorkoutDataItemDetails details) {
        this.details = details;
    }
}
