package com.jawbone.upplatformsdk.datamodel.HeartRateData;

import java.util.List;

public class HeartRateItems {
    private List<HeartRateItem> items;

    public HeartRateItems(){

    }

    public List<HeartRateItem> getItems() {
        return items;
    }

    public void setItems(List<HeartRateItem> items) {
        this.items = items;
    }
}
