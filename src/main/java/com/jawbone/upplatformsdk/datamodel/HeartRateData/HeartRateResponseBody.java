package com.jawbone.upplatformsdk.datamodel.HeartRateData;


import com.jawbone.upplatformsdk.datamodel.Links;

public class HeartRateResponseBody {
    private HeartRateItems items;
    private Links links;
    private int size;

    public HeartRateResponseBody() {
    }

    public HeartRateItems getItems() {
        return items;
    }

    public void setItems(HeartRateItems items) {
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
