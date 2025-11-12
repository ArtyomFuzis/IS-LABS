package com.fuzis.controller;

import com.fuzis.database.PersonRepository;
import com.fuzis.service.PersonService;
import com.fuzis.transferdata.ChangeDTO;
import com.fuzis.transferdata.SelectDTO;
import com.fuzis.entity.*;
import com.fuzis.service.UtilityService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
@Path("/operations/person")
public class PersonController {

    @Inject
    PersonService service;

    @Inject
    UtilityService utilsService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getAllPeople() {
        return new SelectDTO<>(true, service.getAll());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getPersonById(@PathParam("id") Integer id) {
        return new SelectDTO<>(true, service.getById(id));
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deletePersonById(@PathParam("id") Integer id) {
        service.deleteById(id);
        return new ChangeDTO(true);
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO createPerson(@FormParam("name") String name,
                                  @FormParam("eye_color_id") Integer eyeColorId,
                                  @FormParam("hair_color_id") Integer hairColorId,
                                  @FormParam("location_id") Integer locationId,
                                  @FormParam("passport_id") String passportId,
                                  @FormParam("nationality_id") Integer nationalityId,
                                  @FormParam("id") Integer id) {
        service.create(name,eyeColorId,hairColorId,locationId,passportId,nationalityId,id);
        return new ChangeDTO(true);
    }

    @GET
    @Path("/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getPage(@PathParam("page") Integer page) {
        return new SelectDTO<>(true, service.getPage(page));
    }

    @GET
    @Path("/sorted/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getSorted(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        List<Person> res = service.getSorted(page,field,reversed);
        if (res != null) {
            return new SelectDTO<>(true, res);
        }
        return new SelectDTO<>(false, null);
    }

    @GET
    @Path("/filtered/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getFiltered(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("filter") @DefaultValue("") String filter) {
        List<Person> res = service.getFiltered(page,field,filter);
        if (res != null) {
            return new SelectDTO<>(true, res);
        }
        return new SelectDTO<>(false, null);
    }
}
