package com.zenwork.task.controller;

import com.zenwork.task.repository.TaskDB;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
public class AdminRestController {

    private final Validator validator;

    public AdminRestController(Validator validator) {
        this.validator = validator;
    }

    @GET
    public Response getAdmin() {
        return Response.ok("Hello").build();
    }
}
