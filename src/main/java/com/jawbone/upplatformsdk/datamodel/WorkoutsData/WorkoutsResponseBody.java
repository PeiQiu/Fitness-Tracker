package com.jawbone.upplatformsdk.datamodel.WorkoutsData;


import com.jawbone.upplatformsdk.datamodel.Links;

import java.util.List;

public class WorkoutsResponseBody {
    private List<WorkoutDataItem> items;
    private Links links;
    private int size;

    public WorkoutsResponseBody(){

    }

    public List<WorkoutDataItem> getItems() {
        return items;
    }

    public void setItems(List<WorkoutDataItem> items) {
        this.items = items;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
