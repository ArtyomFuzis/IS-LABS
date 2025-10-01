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
@Path("/operations/location")
public class LocationCRUDController {

    @Inject
    DBService dbService;

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Location> getAllLocations() {
       var allObjects = dbService.getAll(Location.class);
       return new SelectDTO<>(true, allObjects);
    }

    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Location> getLocationById(@PathParam("id") Integer id) {
        var obj = dbService.get(id, Location.class);
        return new SelectDTO<>(true, Collections.singletonList(obj));
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteLocationById(@PathParam("id") Integer id) {
        dbService.remove(dbService.get(id, Location.class));
        return new ChangeDTO(true);
    }

    @POST
    @Path("/create/")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO createLocation(@FormParam("name") String name,
                                    @FormParam("x") Double x,
                                    @FormParam("y") Double y,
                                    @FormParam("z") Double z) {
        dbService.save(new Location(name,x,y,z));
        return new ChangeDTO(true);
    }

    @GET
    @Path("/get/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Location> getPage(@PathParam("page") Integer page) {
        var obj = dbService.getPage(page, Location.class);
        return new SelectDTO<>(true, obj);
    }

    @GET
    @Path("/get/{id}/people")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getPeople(@PathParam("id") Integer id) {
        var obj = dbService.get(id, Location.class);
        var res = obj.getPeople();
        return new SelectDTO<>(true, res);
    }

    @GET
    @Path("/get/filtered/name/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Location> getFiltered(@PathParam("page") Integer page, @QueryParam("filter") @DefaultValue("") String filter) {
        var obj = dbService.getFilteredPage(page,"name", filter, Location.class);
        return new SelectDTO<>(true, obj);
    }

    @GET
    @Path("/get/sorted/name/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Location> getSorted(@PathParam("page") Integer page, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        var obj = dbService.getSortedPage(page,"name", reversed, Location.class);
        return new SelectDTO<>(true, obj);
    }
}
