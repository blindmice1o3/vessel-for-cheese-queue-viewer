package com.jackingaming.vesselforcheesequeueviewer.order;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private LocalDateTime createdOn;
    private List<MenuItemInfo> menuItemInfos;

    public Order() {
    }

    public Order(LocalDateTime createdOn, List<MenuItemInfo> menuItemInfos) {
        this.createdOn = createdOn;
        this.menuItemInfos = menuItemInfos;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public List<MenuItemInfo> getMenuItemInfos() {
        return menuItemInfos;
    }

    public void setMenuItemInfos(List<MenuItemInfo> menuItemInfos) {
        this.menuItemInfos = menuItemInfos;
    }
}
