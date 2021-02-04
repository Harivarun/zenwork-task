package com.zenwork.task.dao;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Subuser {

    @NotBlank
    @Length(min=2, max=255)
    private String name;

    @Pattern(regexp=".+@.+\\.[a-z]+")
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String storeID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public Subuser(String name,String email,String password,String storeID) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.storeID = storeID;
    }

    public Subuser() {
    }
}
