package com.fuzis.controller;

import com.fuzis.service.database.LocationService;
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
@Path("/operations/location")
public class LocationController {

    @Inject
    LocationService dbService;

    @Inject
    UtilityService utilsService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Location> getAllLocations() {
       return new SelectDTO<>(true, dbService.getAll());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Location> getLocationById(@PathParam("id") Integer id) {
        return new SelectDTO<>(true, Collections.singletonList(dbService.get(id)));
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteLocationById(@PathParam("id") Integer id) {
        dbService.remove(dbService.get(id));
        return new ChangeDTO(true);
    }

    @POST
    @Path("/")
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
    @Path("/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Location> getPage(@PathParam("page") Integer page) {
        return new SelectDTO<>(true, dbService.getPage(page));
    }

    @GET
    @Path("/{id}/people")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Person> getPeople(@PathParam("id") Integer id) {
        var obj = dbService.get(id);
        var res = obj.getPeople();
        return new SelectDTO<>(true, res);
    }

    @GET
    @Path("/sorted/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Location> getSorted(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        if (utilsService.getFilterableFields(Location.class).contains(field)) {
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
    public SelectDTO<Location> getFiltered(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("filter") @DefaultValue("") String filter) {
        if (utilsService.getFilterableFields(Location.class).contains(field)) {
            var obj = dbService.getFilteredPage(page, field, filter);
            return new SelectDTO<>(true, obj);
        }
        else{
            return new SelectDTO<>(false, null);
        }
    }
}
