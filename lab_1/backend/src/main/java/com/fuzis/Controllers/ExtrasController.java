package com.fuzis.Controllers;

import com.fuzis.Data.CalcDTO;
import com.fuzis.Data.ChangeDTO;
import com.fuzis.Data.SelectDTO;
import com.fuzis.Entities.Difficulty;
import com.fuzis.Entities.LabWork;
import com.fuzis.Entities.Person;
import com.fuzis.Services.DBService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Slf4j
@Path("/operations/extra")
public class ExtrasController {
    @Inject
    DBService dbService;

    @DELETE
    @Path("/deleteAllLabsByAuthor/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteAllLabsByAuthor(@PathParam("id") Integer id) {
        var author = dbService.get(id, Person.class);
        System.out.println("Found size: " + author.getLabs().size());
        author.getLabs().forEach(lab -> dbService.remove(lab));
        return new ChangeDTO(true);
    }

    @GET
    @Path("/maximumPointSum")
    @Produces(MediaType.APPLICATION_JSON)
    public CalcDTO<Double> getMaximumPointSum() {
       return new CalcDTO<>(true, dbService
               .getAll(LabWork.class)
               .stream()
               .map(LabWork::getMaximalPoint)
               .reduce(0.0d, Double::sum));
    }

    @GET
    @Path("/minimalPointUnique")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Double> getMinimalPointUnique() {
        return new SelectDTO<>(true, dbService
                .getAll(LabWork.class)
                .stream()
                .map(LabWork::getMinimalPoint)
                .collect(Collectors.toUnmodifiableSet())
                .stream().toList());
    }

    @POST
    @Path("/increaseDifficulty/{id}/on/{steps}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO postIncreaseDifficulty(@PathParam("id") Integer id, @PathParam("steps") Integer steps) {
        var lab = dbService.get(id, LabWork.class);
        var new_difficulty = dbService.get(lab.getDifficulty().getId()+steps, Difficulty.class);
        if(new_difficulty == null) {
            return new ChangeDTO(false);
        }
        else{
            lab.setDifficulty(new_difficulty);
            dbService.save(lab);
            return new ChangeDTO(true);
        }
    }

    @DELETE
    @Path("/deleteLabWorkFromDiscipline/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteLabFromDiscipline(@PathParam("id") Integer id) {
        var lab = dbService.get(id, LabWork.class);
        lab.setDiscipline(null);
        dbService.merge(lab);
        return new ChangeDTO(true);
    }
}
