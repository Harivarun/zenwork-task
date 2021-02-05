package com.zenwork.task.controller;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.zenwork.task.dao.Subuser;
import com.zenwork.task.service.SubuserService;
import org.bson.Document;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Path("/subuser")
@Produces(MediaType.APPLICATION_JSON)
public class SubUserRestController {

    private final Validator validator;
    private MongoCollection<Document> collection;
    private final SubuserService subuserService;


    public SubUserRestController(MongoCollection<Document> collection, Validator validator,SubuserService subuserService) {
        this.validator = validator;
        this.collection = collection;
        this.subuserService = subuserService;
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getSubuser(Subuser subuser){
        Document subuserDoc = subuserService.findSubuser(collection, subuser.getEmail());
        Map<String, String> response = new HashMap<>();
        if(subuserDoc != null){
            if(subuser.getPassword().equals(subuserDoc.getString("password"))){
                return Response.ok(subuserDoc).build();
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
    @Path("/add")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSubuser(Subuser subuser){
        Set<ConstraintViolation<Subuser>> violations = validator.validate(subuser);
        if(violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<>();
            for (ConstraintViolation<Subuser> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
        }else{
            Gson gson = new Gson();
            String json = gson.toJson(subuser);
            subuserService.createSubuser(collection, new Document(BasicDBObject.parse(json)));
            Map<String, String> response = new HashMap<>();
            response.put("message", "SubUser created successfully");
            return Response.ok(response).build();
        }
    }


}
