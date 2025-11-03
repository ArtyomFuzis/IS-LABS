package com.fuzis.controller;

import com.fuzis.service.database.PersonService;
import com.fuzis.transferdata.ChangeDTO;
import com.fuzis.transferdata.SelectDTO;
import com.fuzis.entity.*;
import com.fuzis.service.database.IDatabaseService;
import com.fuzis.service.UtilityService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
@Path("/operations/person")
public class PersonController {

    @Inject
    PersonService dbService;

    @Inject
    UtilityService utilsService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getAllPeople() {
       return new SelectDTO<>(true, dbService.getAll());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getPersonById(@PathParam("id") Integer id) {
        return new SelectDTO<>(true, Collections.singletonList(dbService.get(id)));
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deletePersonById(@PathParam("id") Integer id) {
        dbService.remove(dbService.get(id));
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
        Location location = (locationId != null) ? dbService.get(locationId, Location.class) : null;
        if(id != null) {
            dbService.merge(new Person(id, name,dbService.get(eyeColorId, Color.class), dbService.get(hairColorId, Color.class),
                    location, passportId, dbService.get(nationalityId, Country.class)));
            return new ChangeDTO(true);
        }
        dbService.save(new Person(name,dbService.get(eyeColorId, Color.class), dbService.get(hairColorId, Color.class),
                location, passportId, dbService.get(nationalityId, Country.class)));
        return new ChangeDTO(true);
    }

    @GET
    @Path("/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getPage(@PathParam("page") Integer page) {
        return new SelectDTO<>(true, dbService.getPage(page));
    }

    @GET
    @Path("/{id}/labs")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getLabs(@PathParam("id") Integer id) {
        var obj = dbService.get(id);
        var res = obj.getLabs();
        return new SelectDTO<>(true, res);
    }

    @GET
    @Path("/sorted/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getSorted(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        if (utilsService.getFilterableFields(Person.class).contains(field)) {
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
    public SelectDTO<Person> getFiltered(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("filter") @DefaultValue("") String filter) {
        if (utilsService.getFilterableFields(Person.class).contains(field)) {
            var obj = dbService.getFilteredPage(page, field, filter);
            return new SelectDTO<>(true, obj);
        }
        else{
            return new SelectDTO<>(false, null);
        }
    }
}
