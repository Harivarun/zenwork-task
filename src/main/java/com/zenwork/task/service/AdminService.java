package com.zenwork.task.service;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class AdminService {

    public void createAdmin(MongoCollection<Document> collection, Document document) {
        collection.insertOne(document);
    }

    public Document findAdmin(MongoCollection<Document> collection, String email) {
        return collection.find(eq("email",email)).first();
    }
}

