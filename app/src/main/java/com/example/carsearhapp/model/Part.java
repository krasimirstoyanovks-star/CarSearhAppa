package com.example.carsearhapp.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Part implements Serializable {
    @SerializedName("name")
    private String name;
    
    @SerializedName("category")
    private String category;
    
    @SerializedName("price")
    private String price;
    
    @SerializedName("availability")
    private String availability;
    
    @SerializedName(value = "imageUrl", alternate = {"image_url", "image"})
    private String imageUrl;
    
    @SerializedName("description")
    private String description;

    @SerializedName(value = "paymentUrl", alternate = {"payment_url", "link"})
    private String paymentUrl;

    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getPrice() { return price; }
    public String getAvailability() { return availability; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public String getPaymentUrl() { return paymentUrl; }
}