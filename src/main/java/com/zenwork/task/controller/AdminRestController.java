package com.zenwork.task.controller;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.zenwork.task.dao.Admin;
import com.zenwork.task.service.AdminService;
import org.bson.Document;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
public class AdminRestController {

    private MongoCollection<Document> collection;
    private final AdminService adminService;
    private final Validator validator;
    public AdminRestController(MongoCollection<Document> collection,Validator validator, AdminService adminService) {
        this.collection = collection;
        this.validator = validator;
        this.adminService = adminService;
    }

    @GET
    public Response getAdmin() {
        return Response.ok("Hello").build();
    }

    @POST
    @Path("/create")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAdmin(Admin admin){
        Set<ConstraintViolation<Admin>> violations = validator.validate(admin);
        if(violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<>();
            for (ConstraintViolation<Admin> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
        }else{
            Gson gson = new Gson();
            String json = gson.toJson(admin);
            adminService.createAdmin(collection, new Document(BasicDBObject.parse(json)));
            Map<String, String> response = new HashMap<>();
            response.put("message", "Admin created successfully");
            return Response.ok(response).build();
        }
    }
}
