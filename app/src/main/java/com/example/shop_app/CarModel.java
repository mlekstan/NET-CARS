package com.example.shop_app;

public class CarModel {
    String brand, model, yearOfProduction, price;
   // String[] imagesIds;
    int image;

    public CarModel(String brand, String model, String yearOfProduction, String price, int image) {
        this.brand = brand;
        this.model = model;
        this.yearOfProduction = yearOfProduction;
        this.price = price;
        this.image = image;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(String yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


/*
    public CarModel(){}

    public CarModel(String brand, String model, String yearOfProduction, String price, String[] imagesIds) {
        this.brand = brand;
        this.model = model;
        this.yearOfProduction = yearOfProduction;
        this.price = price;
        this.imagesIds = imagesIds;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(String yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String[] getImagesIds() {
        return imagesIds;
    }

    public void setImagesIds(String[] imagesIds) {
        this.imagesIds = imagesIds;
    }

     */
}
