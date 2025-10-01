package com.fuzis.Controllers;

import com.fuzis.Data.ChangeDTO;
import com.fuzis.Data.SelectDTO;
import com.fuzis.Entities.Coordinate;
import com.fuzis.Entities.Discipline;
import com.fuzis.Entities.LabWork;
import com.fuzis.Services.DBService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Path("/operations/discipline")
public class DisciplineCRUDController {

    @Inject
    DBService dbService;

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getAllDisciplines() {
       var allObjects = dbService.getAll(Discipline.class);
       return new SelectDTO<>(true, allObjects);
    }

    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getDisciplineById(@PathParam("id") Integer id) {
        var obj = dbService.get(id, Discipline.class);
        return new SelectDTO<>(true, Collections.singletonList(obj));
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteDisciplineById(@PathParam("id") Integer id) {
        dbService.remove(dbService.get(id, Discipline.class));
        return new ChangeDTO(true);
    }

    @POST
    @Path("/create/")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO createDiscipline(@FormParam("name") String name,
                                      @FormParam("labs_count") Integer labs_count) {
        dbService.save(new Discipline(name,labs_count));
        return new ChangeDTO(true);
    }

    @GET
    @Path("/get/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getPage(@PathParam("page") Integer page) {
        var obj = dbService.getPage(page, Discipline.class);
        return new SelectDTO<>(true, obj);
    }

    @GET
    @Path("/get/{id}/labs")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getLabs(@PathParam("id") Integer id) {
        var obj = dbService.get(id, Discipline.class);
        var res = obj.getLabs();
        return new SelectDTO<>(true, res);
    }

    @GET
    @Path("/get/filtered/name/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getFiltered(@PathParam("page") Integer page, @QueryParam("filter") @DefaultValue("") String filter) {
        var obj = dbService.getFilteredPage(page,"name", filter, Discipline.class);
        return new SelectDTO<>(true, obj);
    }

    @GET
    @Path("/get/sorted/name/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getSorted(@PathParam("page") Integer page, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        var obj = dbService.getSortedPage(page,"name", reversed, Discipline.class);
        return new SelectDTO<>(true, obj);
    }
}
