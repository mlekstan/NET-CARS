package com.example.shop_app;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;


public class BasicCarInfo {
    private String brand, model, yearOfProduction, price, pathToDocument, pathToDocumentFileFolder;
    private Uri mainImageUri;


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

    public String getPathToDocument() {
        return pathToDocument;
    }

    public void setPathToDocument(String pathToDocument) {
        this.pathToDocument = pathToDocument;
    }

    public String getPathToDocumentFileFolder() {
        return pathToDocumentFileFolder;
    }

    public void setPathToDocumentFileFolder(String pathToDocumentFileFolder) {
        this.pathToDocumentFileFolder = pathToDocumentFileFolder;
    }

    public Uri getMainImageUri() {
        return mainImageUri;
    }

    public void setMainImageUri(Uri mainImageUri) {
        this.mainImageUri = mainImageUri;
    }

    public BasicCarInfo() {
        this.brand = "";
        this.model = "";
        this.yearOfProduction = "";
        this.price = "";
        this.mainImageUri = null;
        this.pathToDocument = "";
        this.pathToDocumentFileFolder = "";
    }

    public BasicCarInfo(String brand, String model, String yearOfProduction, String price, Uri mainImageUri, String pathToDocument, String pathToDocumentFileFolder) {
        this.brand = brand;
        this.model = model;
        this.yearOfProduction = yearOfProduction;
        this.price = price;
        this.mainImageUri = mainImageUri;
        this.pathToDocument = pathToDocument;
        this.pathToDocumentFileFolder = pathToDocumentFileFolder;
    }

}

