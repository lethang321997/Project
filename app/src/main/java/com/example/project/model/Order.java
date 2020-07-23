package com.example.project.model;

public class Order {

    private String id;
    private String userId;
    private String productId;
    private int orderedQuantity;
    private int orderedPrice;
    private String orderedAddress;
    private String status;

    public int getOrderedPrice() {
        return orderedPrice;
    }

    public void setOrderedPrice(int orderedPrice) {
        this.orderedPrice = orderedPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
