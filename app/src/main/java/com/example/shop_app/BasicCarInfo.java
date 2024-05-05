package com.example.shop_app;

import android.net.Uri;


public class BasicCarInfo {
    String brand, model, yearOfProduction, price;
    Uri mainImageUri;

    public BasicCarInfo() {
        this.brand = "";
        this.model = "";
        this.yearOfProduction = "";
        this.price = "";
        this.mainImageUri = null;
    }
    public BasicCarInfo(String brand, String model, String yearOfProduction, String price, Uri mainImageUri) {
        this.brand = brand;
        this.model = model;
        this.yearOfProduction = yearOfProduction;
        this.price = price;
        this.mainImageUri = mainImageUri;
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

    public Uri getMainImageUri() {
        return mainImageUri;
    }

    public void setMainImageUri(Uri mainImageUri) {
        this.mainImageUri = mainImageUri;
    }

}
