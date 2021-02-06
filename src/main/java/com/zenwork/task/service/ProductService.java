package com.zenwork.task.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.zenwork.task.dao.Product;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class ProductService {

    public List<Document> getAllProducts(MongoCollection<Document> collection) {
        return  collection.find().into(new ArrayList<>());
    }

    public Document findProduct(MongoCollection<Document> collection, String name) {
        return collection.find(eq("name",name)).first();
    }

    public void createProduct(MongoCollection<Document> collection, Document document) {
        collection.insertOne(document);
    }


    public void updateProduct(MongoCollection<Document> collection, Product product) {
        Bson filter = eq("name", product.getName());
        Bson categoryField = set("category", product.getCategory());
        Bson quantityField = set("quantity",product.getQuantity());
        Bson descriptionField = set("description",product.getQuantity());
        Bson storeID = set("storeID",product.getStoreID());
        Bson productDoc = combine(categoryField,quantityField,descriptionField,storeID);

        collection.updateOne(filter, productDoc);
    }

    public DeleteResult deleteProduct(MongoCollection<Document> collection, String name) {
        return collection.deleteOne(eq("name",name));
    }


}
