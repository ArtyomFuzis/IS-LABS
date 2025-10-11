package com.fuzis.Controllers;

import com.fuzis.Data.ChangeDTO;
import com.fuzis.Data.SelectDTO;
import com.fuzis.Entities.Coordinate;
import com.fuzis.Entities.Discipline;
import com.fuzis.Entities.LabWork;
import com.fuzis.Services.DBService;
import com.fuzis.Services.UtilityService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
@Path("/operations/coordinate")
public class CoordinateCRUDController {
    @Inject
    DBService dbService;

    @Inject
    UtilityService utilsService;

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Coordinate> getAllCoordinates() {
        var allObjects = dbService.getAll(Coordinate.class);
        return new SelectDTO<>(true, allObjects);
    }

    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Coordinate> getCoordinateById(@PathParam("id") Integer id) {
        var obj = dbService.get(id, Coordinate.class);
        return new SelectDTO<>(true, Collections.singletonList(obj));
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteCoordinateById(@PathParam("id") Integer id) {
        dbService.remove(dbService.get(id, Coordinate.class));
        return new ChangeDTO(true);
    }

    @POST
    @Path("/create/")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO createCoordinate(@FormParam("x") Double x,
                                      @FormParam("y") Double y) {
        dbService.save(new Coordinate(x,y));
        return new ChangeDTO(true);
    }

    @GET
    @Path("/get/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Coordinate> getPage(@PathParam("page") Integer page) {
        var obj = dbService.getPage(page, Coordinate.class);
        return new SelectDTO<>(true, obj);
    }

    @GET
    @Path("/get/{id}/labs")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getLabs(@PathParam("id") Integer id) {
        var obj = dbService.get(id, Coordinate.class);
        var res = obj.getLabs();
        return new SelectDTO<>(true, res);
    }

    @GET
    @Path("/get/sorted/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Coordinate> getSorted(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        if (utilsService.getFilterableFields(Coordinate.class).contains(field)) {
            var obj = dbService.getSortedPage(page,field, reversed, Coordinate.class);
            return new SelectDTO<>(true, obj);
        }
        else{
            return new SelectDTO<>(false, null);
        }
    }

    @GET
    @Path("/get/filtered/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Coordinate> getFiltered(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("filter") @DefaultValue("") String filter) {
        if (utilsService.getFilterableFields(Coordinate.class).contains(field)) {
            var obj = dbService.getFilteredPage(page, field, filter, Coordinate.class);
            return new SelectDTO<>(true, obj);
        }
        else{
            return new SelectDTO<>(false, null);
        }
    }
}
