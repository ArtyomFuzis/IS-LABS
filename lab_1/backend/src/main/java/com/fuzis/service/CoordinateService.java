package com.fuzis.service;

import com.fuzis.database.CoordinateRepository;
import com.fuzis.entity.Coordinate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;


import java.util.Collections;
import java.util.List;

@RequestScoped
public class CoordinateService {
    @Inject
    CoordinateRepository repo;

    @Inject
    UtilityService utils;

    public List<Coordinate> getAll(){
        return repo.getAll();
    }

    public List<Coordinate> getById(Integer id){
        return Collections.singletonList(repo.get(id));
    }

    public void deleteById(Integer id){
        repo.remove(repo.get(id));
    }

    public void create(Double x,
                       Double y,
                       Integer id){
        if(id != null) {
            repo.merge(new Coordinate(id,x,y));
        }
        else repo.save(new Coordinate(x,y));
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
