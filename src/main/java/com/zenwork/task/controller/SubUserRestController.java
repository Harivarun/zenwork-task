package com.zenwork.task.controller;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/subuser")
@Produces(MediaType.APPLICATION_JSON)
public class SubUserRestController {
    private final Validator validator;
    public SubUserRestController(Validator validator) {
        this.validator = validator;
    }

    @GET
    public Response getSubUser() {
        return Response.ok("Subuser").build();
    }
}
