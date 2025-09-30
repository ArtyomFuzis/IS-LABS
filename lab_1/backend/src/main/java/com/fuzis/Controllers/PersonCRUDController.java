package com.fuzis.Controllers;

import com.fuzis.Data.ChangeDTO;
import com.fuzis.Data.SelectDTO;
import com.fuzis.Entities.Location;
import com.fuzis.Entities.Person;
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
                                  @FormParam("eye_color") String eyeColor,
                                  @FormParam("hair_color") String hairColor,
                                  @FormParam("location_id") Integer locationId,
                                  @FormParam("passport_id") String passportId,
                                  @FormParam("nationality_id") String nationalityId) {
        if(locationId == null) return new ChangeDTO(false);
        Location location = dbService.get(locationId, Location.class);
        dbService.save(new Person(name,eyeColor, hairColor, location, passportId, nationalityId));
        return new ChangeDTO(true);
    }
}
