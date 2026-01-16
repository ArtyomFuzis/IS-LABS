package com.fuzis.controller;

import com.fuzis.service.ExtrasService;
import com.fuzis.transferdata.CalcDTO;
import com.fuzis.transferdata.ChangeDTO;
import com.fuzis.transferdata.SelectDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Path("/operations/extra")
public class ExtrasController {
    @Inject
    ExtrasService service;

    @DELETE
    @Path("/deleteAllLabsByAuthor/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteAllLabsByAuthor(@PathParam("id") Integer id) {
        service.deleteAllLabsByAuthorId(id);
        return new ChangeDTO(true);
    }

    @GET
    @Path("/maximumPointSum")
    @Produces(MediaType.APPLICATION_JSON)
    public CalcDTO<Double> getMaximumPointSum() {
       return new CalcDTO<>(true, service.getMaximumPointSum());
    }

    @GET
    @Path("/minimalPointUnique")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Double> getMinimalPointUnique() {
        return new SelectDTO<>(true, service.getMinimalPointUnique());
    }

    @POST
    @Path("/increaseDifficulty/{id}/on/{steps}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO postIncreaseDifficulty(@PathParam("id") Integer id, @PathParam("steps") Integer steps) {
        return new ChangeDTO(service.postIncreaseDifficulty(id,steps));
    }

    @DELETE
    @Path("/deleteLabWorkFromDiscipline/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteLabFromDiscipline(@PathParam("id") Integer id) {
        service.deleteLabFromDiscipline(id);
        return new ChangeDTO(true);
    }
}
