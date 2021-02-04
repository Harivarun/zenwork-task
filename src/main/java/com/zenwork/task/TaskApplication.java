package com.zenwork.task;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.zenwork.task.controller.AdminRestController;
import com.zenwork.task.controller.SubUserRestController;
import com.zenwork.task.service.AdminService;
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
        MongoCollection<Document> collection = db.getCollection(configuration.getCollectionName());
        LOGGER.info("Registering RESTful API resources");
        environment.jersey().register(new AdminRestController(collection,environment.getValidator(), new AdminService()));
        environment.jersey().register(new SubUserRestController(environment.getValidator()));
    }
}
