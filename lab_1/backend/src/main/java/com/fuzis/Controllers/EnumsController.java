package com.fuzis.Controllers;

import com.fuzis.Data.ChangeDTO;
import com.fuzis.Data.SelectDTO;
import com.fuzis.Entities.Color;
import com.fuzis.Entities.Country;
import com.fuzis.Entities.Difficulty;
import com.fuzis.Entities.Person;
import com.fuzis.Services.DBService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Path("/getEnumVals/")
public class EnumsController
{
    @Inject
    DBService dbService;

    @GET
    @Path("/color")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Color> getColorVals() {
        var allObjects = dbService.getAll(Color.class);
        return new SelectDTO<>(true, allObjects);
    }

    @GET
    @Path("/country")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Country> getPersonById() {
        var allObjects = dbService.getAll(Country.class);
        return new SelectDTO<>(true, allObjects);
    }

    @GET
    @Path("/difficulty")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Difficulty> deletePersonById() {
        var allObjects = dbService.getAll(Difficulty.class);
        return new SelectDTO<>(true, allObjects);
    }
}
