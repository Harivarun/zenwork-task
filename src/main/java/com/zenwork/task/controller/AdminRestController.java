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

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAdmin(Admin admin){
        Document adminDoc = adminService.findAdmin(collection, admin.getEmail());
        Map<String, String> response = new HashMap<>();
        if(adminDoc != null){
            if(admin.getPassword().equals(adminDoc.getString("password"))){
                return Response.ok(adminDoc).build();
            }else{
                response.put("message", "Password Incorrect");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }
        }else{
            response.put("message", "Invalid Email Address");
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }

    }

    @POST
    @Path("/signup")
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
