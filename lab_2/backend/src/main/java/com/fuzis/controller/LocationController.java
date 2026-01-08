package com.fuzis.controller;

import com.fuzis.service.LocationService;
import com.fuzis.transferdata.ChangeDTO;
import com.fuzis.transferdata.SelectDTO;
import com.fuzis.entity.*;
import com.fuzis.util.Utils;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Path("/operations/location")
public class LocationController {

    @Inject
    LocationService service;

    @Inject
    Utils utilsService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Location> getAllLocations() {
        return new SelectDTO<>(true, service.getAll());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Location> getLocationById(@PathParam("id") Integer id) {
        return new SelectDTO<>(true, service.getById(id));
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteLocationById(@PathParam("id") Integer id) {
        service.deleteById(id);
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
        Integer res = service.create(name,x,y,z,id);
        return new ChangeDTO(true, res);
    }

    @GET
    @Path("/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Location> getPage(@PathParam("page") Integer page) {
        return new SelectDTO<>(true, service.getPage(page));
    }

    @GET
    @Path("/sorted/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Location> getSorted(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        List<Location> res = service.getSorted(page,field,reversed);
        if (res != null) {
            return new SelectDTO<>(true, res);
        }
        return new SelectDTO<>(false, null);
    }

    @GET
    @Path("/filtered/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Location> getFiltered(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("filter") @DefaultValue("") String filter) {
        List<Location> res = service.getFiltered(page,field,filter);
        if (res != null) {
            return new SelectDTO<>(true, res);
        }
        return new SelectDTO<>(false, null);
    }
}
