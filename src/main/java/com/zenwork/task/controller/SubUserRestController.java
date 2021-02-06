package com.zenwork.task.controller;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.zenwork.task.dao.Subuser;
import com.zenwork.task.service.SubuserService;
import org.bson.Document;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
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

    @GET
    @Path("/getAll")
    public Response getAllSubusers(){
        return Response.ok(subuserService.getSubusers(collection)).build();
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
            response.put("message", "Subuser created successfully");
            return Response.ok(response).build();
        }
    }

    @PUT
    @Path("/update")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateSubuser(@NotNull @Valid final Subuser subuser) {

        Set<ConstraintViolation<Subuser>> violations = validator.validate(subuser);
        if(violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<>();
            for (ConstraintViolation<Subuser> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
        }else{
            subuserService.updateSubuser(collection, subuser);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Subuser updated successfully");
            return Response.ok(response).build();
        }
    }

    @DELETE
    @Timed
    @Path("/delete/{email}")
    public Response deleteSubuser(@PathParam("email") final String email) {
        DeleteResult result = subuserService.deleteSubuser(collection, email);
        Map<String, String> response = new HashMap<>();
        Document doc = subuserService.findSubuser(collection,email);
        if(doc == null){
            response.put("message", "Subuser with email: " + email + " deleted successfully");
            return Response.ok(response).build();
        }else {
            response.put("message","Subuser not found");
            return  Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }
}
