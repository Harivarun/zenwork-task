package com.zenwork.task.service;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class SubuserService {


    public void createSubuser(MongoCollection<Document> collection, Document document) {
         collection.insertOne(document);
    }

    public Document findSubuser(MongoCollection<Document> collection, String email) {
        return collection.find(eq("email",email)).first();
    }
}
