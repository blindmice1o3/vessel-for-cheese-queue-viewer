package com.jackingaming.vesselforcheesequeueviewer.order;

import java.util.List;

public class OrderDTO {
    private List<Order> orders;

    public OrderDTO() {
    }

    public OrderDTO(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
