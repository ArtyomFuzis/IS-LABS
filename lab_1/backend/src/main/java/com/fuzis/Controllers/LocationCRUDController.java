package com.fuzis.Controllers;

import com.fuzis.Data.ChangeDTO;
import com.fuzis.Data.SelectDTO;
import com.fuzis.Entities.Discipline;
import com.fuzis.Entities.Location;
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
}
