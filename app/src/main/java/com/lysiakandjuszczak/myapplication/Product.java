package com.lysiakandjuszczak.myapplication;

/**
 * Created by dawid on 24.05.17.
 */

public class Product {

    private String name;
    private Double prize;
    private String category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrize() {
        return prize;
    }

    public void setPrize(Double prize) {
        this.prize = prize;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
