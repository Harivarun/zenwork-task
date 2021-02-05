package com.zenwork.task.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.zenwork.task.dao.Subuser;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class SubuserService {


    public void createSubuser(MongoCollection<Document> collection, Document document) {
         collection.insertOne(document);
    }

    public Document findSubuser(MongoCollection<Document> collection, String email) {
        return collection.find(eq("email",email)).first();
    }

    public void updateSubuser(MongoCollection<Document> collection, Subuser subuser) {
        Bson filter = eq("email", subuser.getEmail());
        Bson nameField = set("name", subuser.getName());
        Bson passwordField = set("password",subuser.getPassword());
        Bson storeID = set("storeID",subuser.getStoreID());
        Bson subuserDoc = combine(nameField,passwordField,storeID);

        UpdateResult updateResult = collection.updateOne(filter, subuserDoc);
    }

    public List<Document> getSubusers(MongoCollection<Document> collection) {
        return collection.find().into(new ArrayList<>());
    }

    public DeleteResult deleteSubuser(MongoCollection<Document> collection, String email) {
       return collection.deleteOne(eq("email",email));
    }
}
