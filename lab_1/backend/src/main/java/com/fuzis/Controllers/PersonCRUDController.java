package com.fuzis.Controllers;

import com.fuzis.Data.ChangeDTO;
import com.fuzis.Data.SelectDTO;
import com.fuzis.Entities.*;
import com.fuzis.Services.DBService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
@Path("/operations/person")
public class PersonCRUDController {

    @Inject
    DBService dbService;

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getAllPeople() {
       var allObjects = dbService.getAll(Person.class);
       return new SelectDTO<>(true, allObjects);
    }

    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getPersonById(@PathParam("id") Integer id) {
        var obj = dbService.get(id, Person.class);
        return new SelectDTO<>(true, Collections.singletonList(obj));
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deletePersonById(@PathParam("id") Integer id) {
        dbService.remove(dbService.get(id, Person.class));
        return new ChangeDTO(true);
    }

    @POST
    @Path("/create/")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO createPerson(@FormParam("name") String name,
                                  @FormParam("eye_color_id") Integer eyeColorId,
                                  @FormParam("hair_color_id") Integer hairColorId,
                                  @FormParam("location_id") Integer locationId,
                                  @FormParam("passport_id") String passportId,
                                  @FormParam("nationality_id") Integer nationalityId) {
        if(locationId == null) return new ChangeDTO(false);
        Location location = dbService.get(locationId, Location.class);
        dbService.save(new Person(name,dbService.get(eyeColorId, Color.class), dbService.get(hairColorId, Color.class),
                location, passportId, dbService.get(nationalityId, Country.class)));
        return new ChangeDTO(true);
    }

    @GET
    @Path("/get/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getPage(@PathParam("page") Integer page) {
        var obj = dbService.getPage(page, Person.class);
        return new SelectDTO<>(true, obj);
    }

    @GET
    @Path("/get/{id}/labs")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getLabs(@PathParam("id") Integer id) {
        var obj = dbService.get(id, Person.class);
        var res = obj.getLabs();
        return new SelectDTO<>(true, res);
    }

    @GET
    @Path("/get/filtered/name/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getFilteredName(@PathParam("page") Integer page, @QueryParam("filter") @DefaultValue("") String filter) {
        var obj = dbService.getFilteredPage(page,"name", filter, Person.class);
        return new SelectDTO<>(true, obj);
    }

    @GET
    @Path("/get/sorted/name/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getSortedName(@PathParam("page") Integer page, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        var obj = dbService.getSortedPage(page,"name", reversed, Person.class);
        return new SelectDTO<>(true, obj);
    }

    @GET
    @Path("/get/filtered/passport/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getFilteredPassport(@PathParam("page") Integer page, @QueryParam("filter") @DefaultValue("") String filter) {
        var obj = dbService.getFilteredPage(page,"passportId", filter, Person.class);
        return new SelectDTO<>(true, obj);
    }

    @GET
    @Path("/get/sorted/passport/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getSortedPassport(@PathParam("page") Integer page, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        var obj = dbService.getSortedPage(page,"passportId", reversed, Person.class);
        return new SelectDTO<>(true, obj);
    }
}
