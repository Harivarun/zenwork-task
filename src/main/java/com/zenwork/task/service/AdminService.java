package com.zenwork.task.service;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class AdminService {

    public void createAdmin(MongoCollection<Document> collection, Document document) {
        collection.insertOne(document);
    }
}
