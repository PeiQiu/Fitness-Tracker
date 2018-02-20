package com.jawbone.upplatformsdk.datamodel.Summary;


import com.jawbone.upplatformsdk.datamodel.Links;

import java.util.List;

public class JawboneMoveResponseBody {
    private List<JawboneMoveItem> items;
    private Links links;
    private int size;

    public JawboneMoveResponseBody(){

    }

    public List<JawboneMoveItem> getItems() {
        return items;
    }

    public void setItems(List<JawboneMoveItem> items) {
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
