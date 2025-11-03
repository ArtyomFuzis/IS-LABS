package com.fuzis.controller;

import com.fuzis.service.database.LabWorkService;
import com.fuzis.transferdata.ChangeDTO;
import com.fuzis.transferdata.SelectDTO;
import com.fuzis.entity.*;
import com.fuzis.service.database.IDatabaseService;
import com.fuzis.service.UtilityService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;

@Slf4j
@Path("/operations/labWork")
public class LabWorkController {

    @Inject
    LabWorkService dbService;

    @Inject
    UtilityService utilsService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getAllLabs() {
        return new SelectDTO<>(true, dbService.getAll());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getLabById(@PathParam("id") Integer id) {
        return new SelectDTO<>(true, Collections.singletonList( dbService.get(id)));
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteLabById(@PathParam("id") Integer id) {
        dbService.remove(dbService.get(id));
        return new ChangeDTO(true);
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO createLab(@FormParam("name") String name,
                                    @FormParam("coordinate_id") Integer coordinate_id,
                                        @FormParam("creation_date") String creation_date_str,
                                        @FormParam("description") String description,
                                    @FormParam("difficulty_id") Integer difficulty_id,
                                    @FormParam("discipline_id") Integer discipline_id,
                                    @FormParam("minimal_point") Double minimal_point,
                                    @FormParam("maximal_point") Double maximal_point,
                                    @FormParam("author_id") Integer author_id,
                                    @FormParam("id") Integer id
                                    ) {
        var coordinate = dbService.get(coordinate_id, Coordinate.class);
        var difficulty = dbService.get(difficulty_id, Difficulty.class);
        var discipline = dbService.get(discipline_id, Discipline.class);
        var author = dbService.get(author_id, Person.class);
        var creation_date = creation_date_str == null ? ZonedDateTime.now() : Instant.ofEpochSecond(Long.parseLong(creation_date_str)).atZone(ZoneId.systemDefault());
        if(id != null) {
            dbService.merge(new LabWork(id, name, coordinate, creation_date, description, difficulty, discipline, minimal_point, maximal_point, author));
            return new ChangeDTO(true);
        }
        dbService.save(new LabWork(name, coordinate, creation_date, description, difficulty, discipline, minimal_point, maximal_point, author));
        return new ChangeDTO(true);
    }

    @GET
    @Path("/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getPage(@PathParam("page") Integer page) {
        return new SelectDTO<>(true, dbService.getPage(page));
    }

    @GET
    @Path("/sorted/{field}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<LabWork> getSorted(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("reversed") @DefaultValue("false") Boolean reversed) {
        if (utilsService.getFilterableFields(LabWork.class).contains(field)) {
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
    public SelectDTO<LabWork> getFiltered(@PathParam("page") Integer page, @PathParam("field") String field, @QueryParam("filter") @DefaultValue("") String filter) {
        if (utilsService.getFilterableFields(LabWork.class).contains(field)) {
            var obj = dbService.getFilteredPage(page, field, filter);
            return new SelectDTO<>(true, obj);
        }
        else{
            return new SelectDTO<>(false, null);
        }
    }
}
