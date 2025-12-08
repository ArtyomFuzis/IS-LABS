package com.fuzis.controller;

import com.fuzis.database.EnumsRepository;
import com.fuzis.service.EnumsService;
import com.fuzis.transferdata.SelectDTO;
import com.fuzis.entity.Color;
import com.fuzis.entity.Country;
import com.fuzis.entity.Difficulty;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Path("/EnumVals/")
public class EnumsController
{
    @Inject
    EnumsService service;

    @GET
    @Path("/color")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Color> getColorVals() {
        return new SelectDTO<>(true, service.getColorVals());
    }

    @GET
    @Path("/country")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Country> getCountryVals() {
        return new SelectDTO<>(true, service.getCountryVals());
    }

    @GET
    @Path("/difficulty")
    @Produces(MediaType.APPLICATION_JSON)
    public SelectDTO<Difficulty> getDifficultyVals() {
        return new SelectDTO<>(true, service.getDifficultyVals());
    }
}
