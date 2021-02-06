package com.zenwork.task.dao;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Product {

    @NotNull
    @Length(min=2, max=255)
    private String name;

    @NotNull
    private String category;

    @NotNull
    private long quantity;

    @NotNull
    private String description;

    private String storeID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public Product() {
    }

    public Product(String name, String category, long quantity, String description,String storeID) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.description = description;
        this.storeID = storeID;
    }
}
