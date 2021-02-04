package com.zenwork.task.dao;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Store {

    @NotNull
    @Length(min=2, max=255)
    private String name;

    @NotNull
    private String location;

    @Pattern(regexp="(0/91)?[7-9][0-9]{9}")
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Store(String name,String location, String phone) {
        this.name = name;
        this.location = location;
        this.phone = phone;
    }

    public Store() {
    }
}
