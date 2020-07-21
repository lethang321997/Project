package com.example.project.model;

public class Order {

    private String id;
    private User user;
    private Product product;
    private int orderedQuantity;
    private String orderedAddress;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(int orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public String getOrderedAddress() {
        return orderedAddress;
    }

    public void setOrderedAddress(String orderedAddress) {
        this.orderedAddress = orderedAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
