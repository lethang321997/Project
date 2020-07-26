package com.example.project.model;

public class SoldProduct {
    String id;
    String idProduct;
    String nameProduct;
    String idSeller;
    String idBuyer;
    int quantity;
    int price;
    String image;

    public SoldProduct() {
    }

    public SoldProduct(String id, String idProduct, String nameProduct, String idSeller, String idBuyer, int quantity, int price, String image) {
        this.id = id;
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.idSeller = idSeller;
        this.idBuyer = idBuyer;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getIdSeller() {
        return idSeller;
    }

    public void setIdSeller(String idSeller) {
        this.idSeller = idSeller;
    }

    public String getIdBuyer() {
        return idBuyer;
    }

    public void setIdBuyer(String idBuyer) {
        this.idBuyer = idBuyer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
