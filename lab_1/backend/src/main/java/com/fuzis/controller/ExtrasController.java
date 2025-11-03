package com.fuzis.controller;

import com.fuzis.service.database.ExtrasService;
import com.fuzis.transferdata.CalcDTO;
import com.fuzis.transferdata.ChangeDTO;
import com.fuzis.transferdata.SelectDTO;
import com.fuzis.entity.Difficulty;
import com.fuzis.entity.LabWork;
import com.fuzis.entity.Person;
import com.fuzis.service.database.IDatabaseService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Slf4j
@Path("/operations/extra")
public class ExtrasController {
    @Inject
    ExtrasService dbService;

    @DELETE
    @Path("/deleteAllLabsByAuthor/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO deleteAllLabsByAuthor(@PathParam("id") Integer id) {
        var author = dbService.getPerson(id);
        System.out.println("Found size: " + author.getLabs().size());
        author.getLabs().forEach(lab -> dbService.remove(lab));
        return new ChangeDTO(true);
    }

    @GET
    @Path("/maximumPointSum")
    @Produces(MediaType.APPLICATION_JSON)
    public CalcDTO<Double> getMaximumPointSum() {
       return new CalcDTO<>(true, dbService
               .getAllLabWorks()
               .stream()
               .map(LabWork::getMaximalPoint)
               .reduce(0.0d, Double::sum));
    }

    @GET
    @Path("/minimalPointUnique")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Double> getMinimalPointUnique() {
        return new SelectDTO<>(true, dbService
                .getAllLabWorks()
                .stream()
                .map(LabWork::getMinimalPoint)
                .collect(Collectors.toUnmodifiableSet())
                .stream().toList());
    }

    @POST
    @Path("/increaseDifficulty/{id}/on/{steps}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChangeDTO postIncreaseDifficulty(@PathParam("id") Integer id, @PathParam("steps") Integer steps) {
        var lab = dbService.getLabWork(id);
        var new_difficulty = dbService.getDifficulty(lab.getDifficulty().getId()+steps);
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
        var lab = dbService.getLabWork(id);
        lab.setDiscipline(null);
        dbService.merge(lab);
        return new ChangeDTO(true);
    }
}
