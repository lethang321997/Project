package com.example.project.model;

public class District {
    private int Id;
    private String name;

    public District() {
    }

    public District(int id, String name) {
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
