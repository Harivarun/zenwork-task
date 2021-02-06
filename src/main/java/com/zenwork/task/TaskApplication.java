package com.zenwork.task;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.zenwork.task.controller.AdminRestController;
import com.zenwork.task.controller.ProductRestController;
import com.zenwork.task.controller.SubUserRestController;
import com.zenwork.task.service.AdminService;
import com.zenwork.task.service.ProductService;
import com.zenwork.task.service.StoreService;
import com.zenwork.task.service.SubuserService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskApplication extends Application<TaskConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskApplication.class);

    @Override
    public void initialize(Bootstrap<TaskConfiguration> b) {
    }

    public static void main(String[] args) throws Exception {
        new TaskApplication().run(args);
    }

    @Override
    public void run(TaskConfiguration configuration, Environment environment){
        LOGGER.info("Registering Task resources");
        MongoClient mongoClient = new MongoClient(configuration.getMongoHost(), configuration.getMongoPort());
        MongoDatabase db = mongoClient.getDatabase(configuration.getMongoDB());
        MongoCollection<Document> adminCollection = db.getCollection(configuration.getAdminCollectionName());
        MongoCollection<Document> storeCollection = db.getCollection(configuration.getStoreCollectionName());
        MongoCollection<Document> subuserCollection = db.getCollection(configuration.getSubuserCollectionName());
        MongoCollection<Document> productCollection = db.getCollection(configuration.getProductCollectionName());
        environment.jersey().register(new AdminRestController(adminCollection,environment.getValidator(), new AdminService()));
        environment.jersey().register(new SubUserRestController(subuserCollection,environment.getValidator(), new SubuserService()));
        environment.jersey().register(new ProductRestController(productCollection,environment.getValidator(),new ProductService()));
    }
}
