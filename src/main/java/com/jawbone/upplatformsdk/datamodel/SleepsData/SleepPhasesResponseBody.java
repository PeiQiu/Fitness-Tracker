package com.jawbone.upplatformsdk.datamodel.SleepsData;


import java.util.List;

public class SleepPhasesResponseBody {
    private List<SleepPhasesItem> items;
    private Integer size;

    public SleepPhasesResponseBody() {
    }

    public List<SleepPhasesItem> getItems() {
        return items;
    }

    public void setItems(List<SleepPhasesItem> items) {
        this.items = items;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
