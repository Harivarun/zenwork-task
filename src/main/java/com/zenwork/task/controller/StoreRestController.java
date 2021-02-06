package com.zenwork.task.controller;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.zenwork.task.dao.Product;
import com.zenwork.task.dao.Store;
import com.zenwork.task.service.StoreService;
import com.zenwork.task.service.SubuserService;
import org.bson.Document;
import org.glassfish.jersey.message.internal.WriterInterceptorExecutor;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/store")
@Produces(MediaType.APPLICATION_JSON)
public class StoreRestController {

    private final Validator validator;
    private MongoCollection<Document> collection;
    private final StoreService storeService;

    public StoreRestController(MongoCollection<Document> collection, Validator validator, StoreService storeService) {
        this.validator = validator;
        this.collection = collection;
        this.storeService = storeService;
    }

    @GET
    @Path("/get/{name}")
    public Response getStore(@PathParam("name") String name){
        Document storeDoc = storeService.findStore(collection, name);
        Map<String, String> response = new HashMap<>();
        if(storeDoc != null){
            return Response.ok(storeDoc).build();
        }else{
            response.put("message", "Store is not available");
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @GET
    @Path("/getAll")
    public Response getAllStores(){
        return Response.ok(storeService.getAllStores(collection)).build();
    }

    @POST
    @Path("/add")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProduct(Store store){
        Set<ConstraintViolation<Store>> violations = validator.validate(store);
        if(violations.size() > 0) {
            return getResponse(violations);
        }else{
            Gson gson = new Gson();
            String json = gson.toJson(store);
            storeService.createProduct(collection, new Document(BasicDBObject.parse(json)));
            Map<String, String> response = new HashMap<>();
            response.put("message", "store created successfully");
            return Response.ok(response).build();
        }
    }

    @PUT
    @Path("/update")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProduct(@NotNull @Valid final Store store) {
        Map<String, String> response = new HashMap<>();
        Set<ConstraintViolation<Store>> violations = validator.validate(store);
        if(violations.size() > 0) {
            return getResponse(violations);
        }else{
            Document doc = storeService.findStore(collection,store.getName());
            if(doc != null){
                storeService.updateStore(collection, store);
                response.put("message", "product updated successfully");
                return Response.ok(response).build();
            }else{
                response.put("message", "product not found");
                return Response.ok(response).build();
            }

        }
    }

    @DELETE
    @Timed
    @Path("/delete/{name}")
    public Response deleteStore(@PathParam("name") final String name) {
        Map<String, String> response = new HashMap<>();
        Document doc = storeService.findStore(collection,name);
        if(doc == null){
            response.put("message", "store not found");
            return  Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }else {
            DeleteResult result = storeService.deleteStore(collection, name);
            response.put("message","store deleted");
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        }
    }

    private Response getResponse(Set<ConstraintViolation<Store>> violations) {
        ArrayList<String> validationMessages = new ArrayList<>();
        for (ConstraintViolation<Store> violation : violations) {
            validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
    }
}
