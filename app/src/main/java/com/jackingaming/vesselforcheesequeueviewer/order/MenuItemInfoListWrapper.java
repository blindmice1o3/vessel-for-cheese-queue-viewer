package com.jackingaming.vesselforcheesequeueviewer.order;

import java.util.List;

public class MenuItemInfoListWrapper {
    List<MenuItemInfo> menuItemInfos;

    public MenuItemInfoListWrapper() {
    }

    public MenuItemInfoListWrapper(List<MenuItemInfo> menuItemInfos) {
        this.menuItemInfos = menuItemInfos;
    }

    public List<MenuItemInfo> getMenuItemInfos() {
        return menuItemInfos;
    }

    public void setMenuItemInfos(List<MenuItemInfo> menuItemInfos) {
        this.menuItemInfos = menuItemInfos;
    }
}
