package com.jawbone.upplatformsdk.datamodel.HeartRateData;

public class HeartRateItem {
    private String xid;
    private String title;
    private int time_created;
    private int time_updated;
    private int date;
    private float place_lat;
    private float place_lon;
    private int place_acc;
    private String place_name;
    private int resting_heartrate;
    private TimeZone details;


    public HeartRateItem() {

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

    public int getTime_created() {
        return time_created;
    }

    public void setTime_created(int time_created) {
        this.time_created = time_created;
    }

    public int getTime_updated() {
        return time_updated;
    }

    public void setTime_updated(int time_updated) {
        this.time_updated = time_updated;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public float getPlace_lat() {
        return place_lat;
    }

    public void setPlace_lat(float place_lat) {
        this.place_lat = place_lat;
    }

    public float getPlace_lon() {
        return place_lon;
    }

    public void setPlace_lon(float place_lon) {
        this.place_lon = place_lon;
    }

    public int getPlace_acc() {
        return place_acc;
    }

    public void setPlace_acc(int place_acc) {
        this.place_acc = place_acc;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public int getResting_heartrate() {
        return resting_heartrate;
    }

    public void setResting_heartrate(int resting_heartrate) {
        this.resting_heartrate = resting_heartrate;
    }
}
