package com.jawbone.upplatformsdk.datamodel.SleepsData;

import com.jawbone.upplatformsdk.datamodel.Links;

import java.util.List;

public class SleepsResponseBodyData {
    private List<SleepsItem> items;
    private Links links;
    private int size;

    public SleepsResponseBodyData() {
    }

    public List<SleepsItem> getItems() {
        return items;
    }

    public void setItems(List<SleepsItem> items) {
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
