package com.example.prototype2;

public class Item {

    private String name;

    private int id;
    private String type;
    private int imageResourceId;



    public Item(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

}
