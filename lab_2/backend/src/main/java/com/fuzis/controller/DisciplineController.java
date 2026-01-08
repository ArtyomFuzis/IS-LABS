package com.fuzis.controller;

import com.fuzis.service.DisciplineService;
import com.fuzis.transferdata.ChangeDTO;
import com.fuzis.transferdata.SelectDTO;
import com.fuzis.entity.Discipline;
import com.fuzis.util.Utils;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Path("/operations/discipline")
public class DisciplineController {

    @Inject
    DisciplineService service;

    @Inject
    Utils utilsService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getAllDisciplines() {
        return new SelectDTO<>(true, service.getAll());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getDisciplineById(@PathParam("id") Integer id) {
        return new SelectDTO<>(true, service.getById(id));
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteDisciplineById(@PathParam("id") Integer id) {
        service.deleteById(id);
        return new ChangeDTO(true);
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO createDiscipline(@FormParam("name") String name,
                                      @FormParam("labs_count") Integer labs_count,
                                      @FormParam("id") Integer id) {
        Integer res = service.create(name,labs_count,id);
        return new ChangeDTO(true, res);
    }

    @GET
    @Path("/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getPage(@PathParam("page") Integer page) {
        return new SelectDTO<>(true, service.getPage(page));
    }


    @GET
    @Path("/sorted/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getSorted(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        List<Discipline> res = service.getSorted(page,field,reversed);
        if (res != null) {
            return new SelectDTO<>(true, res);
        }
        return new SelectDTO<>(false, null);
    }

    @GET
    @Path("/filtered/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getFiltered(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("filter") @DefaultValue("") String filter) {
        List<Discipline> res = service.getFiltered(page,field,filter);
        if (res != null) {
            return new SelectDTO<>(true, res);
        }
        return new SelectDTO<>(false, null);
    }
}
