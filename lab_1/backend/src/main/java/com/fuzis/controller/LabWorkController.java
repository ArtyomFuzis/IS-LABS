package com.fuzis.controller;

import com.fuzis.database.LabWorkRepository;
import com.fuzis.service.LabWorkService;
import com.fuzis.transferdata.ChangeDTO;
import com.fuzis.transferdata.SelectDTO;
import com.fuzis.entity.*;
import com.fuzis.service.UtilityService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Path("/operations/labWork")
public class LabWorkController {

    @Inject
    LabWorkService service;

    @Inject
    UtilityService utilsService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getAllLabs() {
        return new SelectDTO<>(true, service.getAll());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getLabById(@PathParam("id") Integer id) {
        return new SelectDTO<>(true, service.getById(id));
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteLabById(@PathParam("id") Integer id) {
        service.deleteById(id);
        return new ChangeDTO(true);
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO createLab(@FormParam("name") String name,
                                    @FormParam("coordinate_id") Integer coordinate_id,
                                    @FormParam("creation_date") String creation_date_str,
                                    @FormParam("description") String description,
                                    @FormParam("difficulty_id") Integer difficulty_id,
                                    @FormParam("discipline_id") Integer discipline_id,
                                    @FormParam("minimal_point") Double minimal_point,
                                    @FormParam("maximal_point") Double maximal_point,
                                    @FormParam("author_id") Integer author_id,
                                    @FormParam("id") Integer id
                                    ) {
        service.create(name,coordinate_id,creation_date_str,description,difficulty_id,discipline_id,minimal_point,maximal_point,author_id,id);
        return new ChangeDTO(true);
    }

    @GET
    @Path("/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getPage(@PathParam("page") Integer page) {
        return new SelectDTO<>(true, service.getPage(page));
    }

    @GET
    @Path("/sorted/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getSorted(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        List<LabWork> res = service.getSorted(page,field,reversed);
        if (res != null) {
            return new SelectDTO<>(true, res);
        }
        return new SelectDTO<>(false, null);
    }

    @GET
    @Path("/filtered/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getFiltered(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("filter") @DefaultValue("") String filter) {
        List<LabWork> res = service.getFiltered(page,field,filter);
        if (res != null) {
            return new SelectDTO<>(true, res);
        }
        return new SelectDTO<>(false, null);
    }
}
