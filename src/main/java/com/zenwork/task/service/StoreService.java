package com.zenwork.task.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.zenwork.task.dao.Store;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class StoreService {

    public Document findStore(MongoCollection<Document> collection, String name) {
        return collection.find(eq("name",name)).first();
    }

    public List<Document> getAllStores(MongoCollection<Document> collection) {
        return collection.find().into(new ArrayList<>());
    }

    public void createProduct(MongoCollection<Document> collection, Document document) {
        collection.insertOne(document);
    }

    public void updateStore(MongoCollection<Document> collection, Store store) {

        Bson filter = eq("name", store.getName());
        Bson locationField = set("location", store.getLocation());
        Bson phoneField = set("phone",store.getPhone());
        Bson storeDoc = combine(locationField,phoneField);

        collection.updateOne(filter, storeDoc);
    }

    public DeleteResult deleteStore(MongoCollection<Document> collection, String name) {
        return collection.deleteOne(eq("name",name));
    }
}
