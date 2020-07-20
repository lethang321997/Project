package com.example.project.model;

import java.util.ArrayList;

public class Product {
    private String id;
    private String idUser;
    private String name;
    private String brand;
    private int quantity;
    private String color;
    private String type;
    private ArrayList<String> images;
    private int price;
    private String mainImage;

    public Product() {
    }

    public Product(String id, String idUser, String name, String brand, int quantity, String color, String type, int price) {
        this.id = id;
        this.idUser = idUser;
        this.name = name;
        this.brand = brand;
        this.quantity = quantity;
        this.color = color;
        this.type = type;
        this.price = price;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
