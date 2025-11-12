package com.fuzis.service;

import com.fuzis.database.EnumsRepository;
import com.fuzis.entity.Color;
import com.fuzis.entity.Country;
import com.fuzis.entity.Difficulty;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@RequestScoped
public class EnumsService {
    @Inject
    EnumsRepository repo;

    public List<Color> getColorVals(){
        return repo.getColorVals();
    }

    public List<Country> getCountryVals(){
        return repo.getCountryVals();
    }

    public List<Difficulty> getDifficultyVals(){
        return repo.getDifficultyVals();
    }
}
