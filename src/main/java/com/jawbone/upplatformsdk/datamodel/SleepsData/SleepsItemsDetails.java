package com.jawbone.upplatformsdk.datamodel.SleepsData;

public class SleepsItemsDetails {
    private Long awake_time;
    private Long asleep_time;
    private Long awakenings;
    private Long rem;
    private Long light;
    private Long deep;
    private Long awake;
    private Long duration;
    private String tz;

    public SleepsItemsDetails() {
    }

    public Long getAwake_time() {
        return awake_time;
    }

    public void setAwake_time(Long awake_time) {
        this.awake_time = awake_time;
    }

    public Long getAsleep_time() {
        return asleep_time;
    }

    public void setAsleep_time(Long asleep_time) {
        this.asleep_time = asleep_time;
    }

    public Long getAwakenings() {
        return awakenings;
    }

    public void setAwakenings(Long awakenings) {
        this.awakenings = awakenings;
    }

    public Long getRem() {
        return rem;
    }

    public void setRem(Long rem) {
        this.rem = rem;
    }

    public Long getLight() {
        return light;
    }

    public void setLight(Long light) {
        this.light = light;
    }

    public Long getDeep() {
        return deep;
    }

    public void setDeep(Long deep) {
        this.deep = deep;
    }

    public Long getAwake() {
        return awake;
    }

    public void setAwake(Long awake) {
        this.awake = awake;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }
}
