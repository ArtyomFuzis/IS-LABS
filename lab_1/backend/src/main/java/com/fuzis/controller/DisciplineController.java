package com.fuzis.controller;

import com.fuzis.service.database.DisciplineService;
import com.fuzis.transferdata.ChangeDTO;
import com.fuzis.transferdata.SelectDTO;
import com.fuzis.entity.Discipline;
import com.fuzis.entity.LabWork;
import com.fuzis.service.database.IDatabaseService;
import com.fuzis.service.UtilityService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
@Path("/operations/discipline")
public class DisciplineController {

    @Inject
    DisciplineService dbService;

    @Inject
    UtilityService utilsService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getAllDisciplines() {
       return new SelectDTO<>(true, dbService.getAll());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getDisciplineById(@PathParam("id") Integer id) {
        return new SelectDTO<>(true, Collections.singletonList(dbService.get(id)));
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteDisciplineById(@PathParam("id") Integer id) {
        dbService.remove(dbService.get(id));
        return new ChangeDTO(true);
    }

    @POST
    @Path("/")
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
    @Path("/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getPage(@PathParam("page") Integer page) {
        return new SelectDTO<>(true, dbService.getPage(page));
    }

    @GET
    @Path("/{id}/labs")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getLabs(@PathParam("id") Integer id) {
        return new SelectDTO<>(true, dbService.get(id).getLabs());
    }

    @GET
    @Path("/sorted/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getSorted(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        if (utilsService.getFilterableFields(Discipline.class).contains(field)) {
            var obj = dbService.getSortedPage(page,field, reversed);
            return new SelectDTO<>(true, obj);
        }
        else{
            return new SelectDTO<>(false, null);
        }
    }

    @GET
    @Path("/filtered/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getFiltered(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("filter") @DefaultValue("") String filter) {
        if (utilsService.getFilterableFields(Discipline.class).contains(field)) {
            var obj = dbService.getFilteredPage(page, field, filter);
            return new SelectDTO<>(true, obj);
        }
        else{
            return new SelectDTO<>(false, null);
        }
    }
}
