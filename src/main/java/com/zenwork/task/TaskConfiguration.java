package com.zenwork.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class TaskConfiguration extends Configuration {

    @JsonProperty
    @NotEmpty
    public String mongoHost;

    @JsonProperty
    @Min(1)
    @Max(65535)
    public int mongoPort;

    @JsonProperty
    @NotEmpty
    public String mongoDB;

    @JsonProperty
    @NotEmpty
    public String collectionName;

    @JsonProperty
    @NotEmpty
    public String adminCollectionName;

    @JsonProperty
    @NotEmpty
    public String storeCollectionName;

    @JsonProperty
    @NotEmpty
    public String subuserCollectionName;

    @JsonProperty
    @NotEmpty
    public String productCollectionName;


    public String getMongoHost() {
        return mongoHost;
    }

    public void setMongoHost(String mongoHost) {
        this.mongoHost = mongoHost;
    }

    public int getMongoPort() {
        return mongoPort;
    }

    public void setMongoPort(int mongoPort) {
        this.mongoPort = mongoPort;
    }

    public String getMongoDB() {
        return mongoDB;
    }

    public void setMongoDB(String mongoDB) {
        this.mongoDB = mongoDB;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getAdminCollectionName() {
        return adminCollectionName;
    }

    public void setAdminCollectionName(String adminCollectionName) {
        this.adminCollectionName = adminCollectionName;
    }

    public String getStoreCollectionName() {
        return storeCollectionName;
    }

    public void setStoreCollectionName(String storeCollectionName) {
        this.storeCollectionName = storeCollectionName;
    }

    public String getSubuserCollectionName() {
        return subuserCollectionName;
    }

    public void setSubuserCollectionName(String subuserCollectionName) {
        this.subuserCollectionName = subuserCollectionName;
    }

    public String getProductCollectionName() {
        return productCollectionName;
    }

    public void setProductCollectionName(String productCollectionName) {
        this.productCollectionName = productCollectionName;
    }
}
