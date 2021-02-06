package com.zenwork.task.controller;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.zenwork.task.dao.Product;
import com.zenwork.task.dao.Subuser;
import com.zenwork.task.service.ProductService;
import com.zenwork.task.service.SubuserService;
import org.bson.Document;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
public class ProductRestController {
    private final Validator validator;
    private MongoCollection<Document> collection;
    private final ProductService productService;

    public ProductRestController(MongoCollection<Document> collection, Validator validator, ProductService productService) {
        this.validator = validator;
        this.productService = productService;
        this.collection = collection;
    }

    @GET
    @Path("/getAll")
    public Response getAllProducts(){
        return Response.ok(productService.getAllProducts(collection)).build();
    }

    @GET
    @Path("/get/{name}")
    public Response getProduct(@PathParam("name") String name){
        Document productDoc = productService.findProduct(collection, name);
        Map<String, String> response = new HashMap<>();
        if(productDoc != null){
                return Response.ok(productDoc).build();
        }else{
            response.put("message", "Invalid product name");
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @POST
    @Path("/add")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProduct(Product product){
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        if(violations.size() > 0) {
            return getResponse(violations);
        }else{
            Gson gson = new Gson();
            String json = gson.toJson(product);
            productService.createProduct(collection, new Document(BasicDBObject.parse(json)));
            Map<String, String> response = new HashMap<>();
            response.put("message", "product created successfully");
            return Response.ok(response).build();
        }
    }


    @PUT
    @Path("/update")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProduct(@NotNull @Valid final Product product) {

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        if(violations.size() > 0) {
            return getResponse(violations);
        }else{
            productService.updateProduct(collection, product);
            Map<String, String> response = new HashMap<>();
            response.put("message", "product updated successfully");
            return Response.ok(response).build();
        }
    }

    @DELETE
    @Timed
    @Path("/delete/{name}")
    public Response deleteProduct(@PathParam("name") final String name) {
        Map<String, String> response = new HashMap<>();
        Document doc = productService.findProduct(collection,name);
        if(doc == null){
            response.put("message", "product not found");
            return  Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }else {
            DeleteResult result = productService.deleteProduct(collection, name);
            response.put("message","product deleted");
            return Response.ok(response).build();
        }
    }

    private Response getResponse(Set<ConstraintViolation<Product>> violations) {
        ArrayList<String> validationMessages = new ArrayList<>();
        for (ConstraintViolation<Product> violation : violations) {
            validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
    }
}
