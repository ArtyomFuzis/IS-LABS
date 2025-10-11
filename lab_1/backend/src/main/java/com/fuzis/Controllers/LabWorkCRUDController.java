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

import java.time.ZonedDateTime;
import java.util.Collections;

@Slf4j
@Path("/operations/labWork")
public class LabWorkCRUDController {

    @Inject
    DBService dbService;

    @Inject
    UtilityService utilsService;

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getAllLabs() {
        var allObjects = dbService.getAll(LabWork.class);
        return new SelectDTO<>(true, allObjects);
    }

    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getLabById(@PathParam("id") Integer id) {
        var obj = dbService.get(id, LabWork.class);
        return new SelectDTO<>(true, Collections.singletonList(obj));
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteLabById(@PathParam("id") Integer id) {
        dbService.remove(dbService.get(id, LabWork.class));
        return new ChangeDTO(true);
    }

    @POST
    @Path("/create/")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO createLab(@FormParam("name") String name,
                                    @FormParam("coordinate_id") Integer coordinate_id,
                                    @FormParam("creation_date") String creation_date_str,
                                    @FormParam("description") String description,
                                    @FormParam("difficulty_id") Integer difficulty_id,
                                    @FormParam("discipline_id") Integer discipline_id,
                                    @FormParam("minimal_point") Double minimal_point,
                                    @FormParam("maximal_point") Double maximal_point,
                                    @FormParam("author_id") Integer author_id) {
        var coordinate = dbService.get(coordinate_id, Coordinate.class);
        var difficulty = dbService.get(difficulty_id, Difficulty.class);
        var discipline = dbService.get(discipline_id, Discipline.class);
        var author = dbService.get(author_id, Person.class);
        var creation_date = creation_date_str == null ? ZonedDateTime.now() : ZonedDateTime.parse(creation_date_str);
        dbService.save(new LabWork(name, coordinate, creation_date, description, difficulty, discipline, minimal_point, maximal_point, author));
        return new ChangeDTO(true);
    }

    @GET
    @Path("/get/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getPage(@PathParam("page") Integer page) {
        var obj = dbService.getPage(page, LabWork.class);
        return new SelectDTO<>(true, obj);
    }

    @GET
    @Path("/get/sorted/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getSorted(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        if (utilsService.getFilterableFields(LabWork.class).contains(field)) {
            var obj = dbService.getSortedPage(page,field, reversed, LabWork.class);
            return new SelectDTO<>(true, obj);
        }
        else{
            return new SelectDTO<>(false, null);
        }
    }

    @GET
    @Path("/get/filtered/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getFiltered(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("filter") @DefaultValue("") String filter) {
        if (utilsService.getFilterableFields(LabWork.class).contains(field)) {
            var obj = dbService.getFilteredPage(page, field, filter, LabWork.class);
            return new SelectDTO<>(true, obj);
        }
        else{
            return new SelectDTO<>(false, null);
        }
    }
}
