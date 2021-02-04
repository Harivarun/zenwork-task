package com.zenwork.task;

import com.zenwork.task.controller.AdminRestController;
import com.zenwork.task.controller.SubUserRestController;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskApplication extends Application<Configuration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskApplication.class);

    @Override
    public void initialize(Bootstrap<Configuration> b) {
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        LOGGER.info("Registering REST resources");
        environment.jersey().register(new AdminRestController(environment.getValidator()));
        environment.jersey().register(new SubUserRestController(environment.getValidator()));
    }


    public static void main(String[] args) throws Exception {
        new TaskApplication().run(args);
    }
}
