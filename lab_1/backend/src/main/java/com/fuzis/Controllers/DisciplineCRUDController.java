package com.fuzis.Controllers;

import com.fuzis.Data.ChangeDTO;
import com.fuzis.Data.SelectDTO;
import com.fuzis.Entities.Discipline;
import com.fuzis.Entities.LabWork;
import com.fuzis.Entities.Location;
import com.fuzis.Services.DBService;
import com.fuzis.Services.UtilityService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
@Path("/operations/discipline")
public class DisciplineCRUDController {

    @Inject
    DBService dbService;

    @Inject
    UtilityService utilsService;

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
                                      @FormParam("labs_count") Integer labs_count,
                                      @FormParam("id") Integer id) {
        if(id != null) {
            dbService.merge(new Discipline(id,name,labs_count));
            return new ChangeDTO(true);
        }
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
    @Path("/get/sorted/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getSorted(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        if (utilsService.getFilterableFields(Discipline.class).contains(field)) {
            var obj = dbService.getSortedPage(page,field, reversed, Discipline.class);
            return new SelectDTO<>(true, obj);
        }
        else{
            return new SelectDTO<>(false, null);
        }
    }

    @GET
    @Path("/get/filtered/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getFiltered(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("filter") @DefaultValue("") String filter) {
    if (utilsService.getFilterableFields(Discipline.class).contains(field)) {
        var obj = dbService.getFilteredPage(page, field, filter, Discipline.class);
        return new SelectDTO<>(true, obj);
    }
    else{
        return new SelectDTO<>(false, null);
    }
    }
}
