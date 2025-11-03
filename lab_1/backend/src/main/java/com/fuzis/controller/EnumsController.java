package com.fuzis.controller;

import com.fuzis.service.database.EnumsService;
import com.fuzis.transferdata.SelectDTO;
import com.fuzis.entity.Color;
import com.fuzis.entity.Country;
import com.fuzis.entity.Difficulty;
import com.fuzis.service.database.IDatabaseService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Path("/EnumVals/")
public class EnumsController
{
    @Inject
    EnumsService dbService;

    @GET
    @Path("/color")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Color> getColorVals() {
        return new SelectDTO<>(true, dbService.getColorVals());
    }

    @GET
    @Path("/country")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Country> getCountryVals() {
        return new SelectDTO<>(true, dbService.getCountryVals());
    }

    @GET
    @Path("/difficulty")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Difficulty> getDifficultyVals() {
        return new SelectDTO<>(true, dbService.getDifficultyVals());
    }
}
