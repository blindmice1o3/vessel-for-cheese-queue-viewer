package com.jackingaming.vesselforcheesequeueviewer.order;

import java.util.List;

public class MenuItemInfo {
    private String id;
    private String size;
    private List<String> menuItemCustomizations;

    public MenuItemInfo() {
    }

    public MenuItemInfo(String id, String size, List<String> menuItemCustomizations) {
        this.id = id;
        this.size = size;
        this.menuItemCustomizations = menuItemCustomizations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<String> getMenuItemCustomizations() {
        return menuItemCustomizations;
    }

    public void setMenuItemCustomizations(List<String> menuItemCustomizations) {
        this.menuItemCustomizations = menuItemCustomizations;
    }
}
