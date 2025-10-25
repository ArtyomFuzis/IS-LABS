package com.fuzis.Controllers;

import com.fuzis.Data.ChangeDTO;
import com.fuzis.Data.SelectDTO;
import com.fuzis.Entities.*;
import com.fuzis.Services.DBService;
import com.fuzis.Services.UtilityService;
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

    @Inject
    UtilityService utilsService;

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
                                    @FormParam("z") Double z,
                                    @FormParam("id") Integer id) {
        if(id != null) {
            dbService.merge(new Location(id,name,x,y,z));
            return new ChangeDTO(true);
        }
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
    @Path("/get/sorted/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Location> getSorted(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        if (utilsService.getFilterableFields(Location.class).contains(field)) {
            var obj = dbService.getSortedPage(page,field, reversed, Location.class);
            return new SelectDTO<>(true, obj);
        }
        else{
            return new SelectDTO<>(false, null);
        }
    }

    @GET
    @Path("/get/filtered/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Location> getFiltered(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("filter") @DefaultValue("") String filter) {
        if (utilsService.getFilterableFields(Location.class).contains(field)) {
            var obj = dbService.getFilteredPage(page, field, filter, Location.class);
            return new SelectDTO<>(true, obj);
        }
        else{
            return new SelectDTO<>(false, null);
        }
    }
}
