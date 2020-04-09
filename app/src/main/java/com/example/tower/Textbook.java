package com.example.tower;

public class Textbook {

    private String title;
    private String author;
    private long seller;
    private double price;
    private String uniqueID;
    private String imageUrl = "placeholder";
    private String isbn13;

    public Textbook() {
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public Textbook(String title, String author, long seller, double price, String uniqueID) {
        this.title = title;
        this.author = author;
        this.seller = seller;
        this.price = price;
        this.uniqueID = uniqueID;
    }

    public Textbook(String title, String author, String isbn13, String imageUrl) {
        this.title = title;
        this.author = author;
        this.isbn13 = isbn13;
        this.imageUrl = imageUrl;
        this.seller = 0;
        this.price = 0;
    }

    public Textbook(String title) {
        this.title = title;
        this.author = "";
        this.seller = 0;
        this.price = 0;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
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
