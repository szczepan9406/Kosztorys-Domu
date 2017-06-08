package com.lysiakandjuszczak.myapplication;

/**
 * Created by dawid on 24.05.17.
 */

//klasa do opisu produktu

public class Product {
    private long id;
    private String name;
    private double prize;
    private String category;
    private int count;
    private String currency;

    public Product(String name, double prize, int count, String category) {
        this.name = name;
        this.prize = prize;
        this.category = category;
        this.count = count;
    }

    public Product(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrize() {
        return prize;
    }

    public void setPrize(double prize) {
        this.prize = prize;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
