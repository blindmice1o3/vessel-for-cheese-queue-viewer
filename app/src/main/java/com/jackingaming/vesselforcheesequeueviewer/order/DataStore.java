package com.jackingaming.vesselforcheesequeueviewer.order;

import java.util.List;

public class DataStore {
    private List<MenuItemInfoListWrapper> data;

    public DataStore() {
    }

    public DataStore(List<MenuItemInfoListWrapper> data) {
        this.data = data;
    }

    public List<MenuItemInfoListWrapper> getData() {
        return data;
    }

    public void setData(List<MenuItemInfoListWrapper> data) {
        this.data = data;
    }
}
