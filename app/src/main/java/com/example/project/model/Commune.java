package com.example.project.model;

public class Commune {
    private int Id;
    private String name;

    public Commune() {
    }

    public Commune(int id, String name) {
        Id = id;
        this.name = name;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
