package com.fuzis.service;

import com.fuzis.annotation.CacheStatisticsLogging;
import com.fuzis.database.CoordinateRepository;
import com.fuzis.entity.Coordinate;
import com.fuzis.util.Utils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;


import java.util.Collections;
import java.util.List;

@RequestScoped
@CacheStatisticsLogging
public class CoordinateService {
    @Inject
    CoordinateRepository repo;

    @Inject
    Utils utils;

    @Inject
    ValidationService  validation;


    public List<Coordinate> getAll(){
        return repo.getAll();
    }

    public List<Coordinate> getById(Integer id){
        return Collections.singletonList(repo.get(id));
    }

    @Transactional
    public void deleteById(Integer id){
        repo.remove(repo.get(id));
        //repo.flush();
    }

    @Transactional
    public Integer create(Double x,
                       Double y,
                       Integer id){
        Coordinate coordinate;
        if (id != null) {
            coordinate = new Coordinate(id, x, y);
            validation.validateCoordinate(coordinate);
            repo.merge(coordinate);
        } else {
            coordinate = new Coordinate(x, y);
            validation.validateCoordinate(coordinate);
            repo.save(coordinate);
        }
        return coordinate.getId();
    }

    public List<Coordinate> getPage(Integer page){
        return repo.getPage(page);
    }

    public List<Coordinate> getSorted(Integer page, String field, Boolean reversed){
        if (utils.getFilterableFields(Coordinate.class).contains(field)) {
            return repo.getSortedPage(page,field, reversed);
        }
        return null;
    }

    public List<Coordinate> getFiltered(Integer page, String field, String filter){
        if (utils.getFilterableFields(Coordinate.class).contains(field)) {
            return repo.getFilteredPage(page, field, filter);
        }
        return null;
    }

}
