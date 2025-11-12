package com.fuzis.controller;

import com.fuzis.database.CoordinateRepository;
import com.fuzis.service.CoordinateService;
import com.fuzis.transferdata.ChangeDTO;
import com.fuzis.transferdata.SelectDTO;
import com.fuzis.entity.Coordinate;
import com.fuzis.entity.LabWork;
import com.fuzis.service.UtilityService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
@Path("/operations/coordinate")
public class CoordinateController {
    @Inject
    CoordinateService service;

    @Inject
    UtilityService utilsService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Coordinate> getAllCoordinates() {
        return new SelectDTO<>(true, service.getAll());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Coordinate> getCoordinateById(@PathParam("id") Integer id) {
        return new SelectDTO<>(true, service.getById(id));
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteCoordinateById(@PathParam("id") Integer id) {
        service.deleteById(id);
        return new ChangeDTO(true);
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO createCoordinate(@FormParam("x") Double x,
                                      @FormParam("y") Double y,
                                      @FormParam("id") Integer id) {
        service.create(x,y,id);
        return new ChangeDTO(true);
    }

    @GET
    @Path("/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Coordinate> getPage(@PathParam("page") Integer page) {
        return new SelectDTO<>(true, service.getPage(page));
    }


    @GET
    @Path("/sorted/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Coordinate> getSorted(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        List<Coordinate> res = service.getSorted(page,field,reversed);
        if (res != null) {
            return new SelectDTO<>(true, res);
        }
        return new SelectDTO<>(false, null);
    }

    @GET
    @Path("/filtered/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Coordinate> getFiltered(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("filter") @DefaultValue("") String filter) {
        List<Coordinate> res = service.getFiltered(page,field,filter);
        if (res != null) {
            return new SelectDTO<>(true, res);
        }
        return new SelectDTO<>(false, null);
    }
}
