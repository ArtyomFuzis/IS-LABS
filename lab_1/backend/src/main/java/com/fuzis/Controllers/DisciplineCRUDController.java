package com.fuzis.Controllers;

import com.fuzis.Data.ChangeDTO;
import com.fuzis.Data.SelectDTO;
import com.fuzis.Entities.Discipline;
import com.fuzis.Services.DBService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Path("/operations")
public class DisciplineCRUDController {
    private static final Logger logger = LoggerFactory.getLogger(DisciplineCRUDController.class);

    @Inject
    DBService dbService;

    @GET
    @Path("/discipline/get")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getAllDisciplines() {
       try{
           List<Discipline> allDisciplines = dbService.disciplineGetAll();
           return new SelectDTO<>(true, allDisciplines);
       }
       catch(Exception e){
           logger.error(e.getMessage());
           logger.error(Arrays.toString(e.getStackTrace()));
           return new SelectDTO<>(false, null);
       }
    }

    @GET
    @Path("/discipline/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Discipline> getDisciplineById(@PathParam("id") Integer id) {
        try{
            Discipline discipline = dbService.disciplineGet(id);
            return new SelectDTO<>(true, Collections.singletonList(discipline));
        }
        catch(Exception e){
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return new SelectDTO<>(false,null);
        }
    }

    @DELETE
    @Path("/discipline/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteDisciplineById(@PathParam("id") Integer id) {
        try{
            dbService.disciplineRemove(dbService.disciplineGet(id));
            return new ChangeDTO(true);
        }
        catch(Exception e){
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return new ChangeDTO(false);
        }
    }

    @POST
    @Path("/discipline/create/")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO createDiscipline(@FormParam("name") String name,
                                      @FormParam("labs_count") Integer labs_count) {
        try{
            dbService.disciplineSave(new Discipline(name,labs_count));
            return new ChangeDTO(true);
        }
        catch(Exception e){
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return new ChangeDTO(false);
        }
    }
}
