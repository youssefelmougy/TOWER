package com.example.tower;

public class Textbook {

    private String title;
    private String author;
    private long seller;
    private double price;

    public Textbook() {
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Textbook(String title, String author, long seller, double price) {
        this.title = title;
        this.author = author;
        this.seller = seller;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getSeller() {
        return seller;
    }

    public void setSeller(long seller) {
        this.seller = seller;
    }
}
