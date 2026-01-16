package com.fuzis.service;

import com.fuzis.annotation.CacheStatisticsLogging;
import com.fuzis.database.LabWorkRepository;
import com.fuzis.entity.*;
import com.fuzis.util.Utils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;


@RequestScoped
@CacheStatisticsLogging
public class LabWorkService {
    @Inject
    LabWorkRepository repo;

    @Inject
    Utils utils;

    @Inject
    ValidationService validation;

    public List<LabWork> getAll(){
        return repo.getAll();
    }

    public List<LabWork> getById(Integer id){
        return Collections.singletonList(repo.get(id));
    }

    @Transactional
    public void deleteById(Integer id){
        repo.remove(repo.get(id));
    }

    @Transactional
    public Integer create(String name,
                       Integer coordinate_id,
                       String creation_date_str,
                       String description,
                       Integer difficulty_id,
                       Integer discipline_id,
                       Double minimal_point,
                       Double maximal_point,
                       Integer author_id,
                       Integer id){
        var coordinate = repo.get(coordinate_id, Coordinate.class);
        var difficulty = repo.get(difficulty_id, Difficulty.class);
        var discipline = repo.get(discipline_id, Discipline.class);
        var author = repo.get(author_id, Person.class);
        var creation_date = creation_date_str == null ? ZonedDateTime.now() : Instant.ofEpochSecond(Long.parseLong(creation_date_str)).atZone(ZoneId.systemDefault());
        LabWork new_obj;
        if (id != null) {
            new_obj = (new LabWork(id, name, coordinate, creation_date, description, difficulty, discipline, minimal_point, maximal_point, author));
            validation.validateLabWork(new_obj);
            repo.merge(new_obj);
        } else {
            new_obj = (new LabWork(name, coordinate, creation_date, description, difficulty, discipline, minimal_point, maximal_point, author));
            validation.validateLabWork(new_obj);
            repo.save(new_obj);
        }
        return new_obj.getId();
    }

    public List<LabWork> getPage(Integer page){
        return repo.getPage(page);
    }

    public List<LabWork> getSorted(Integer page, String field, Boolean reversed){
        if (utils.getFilterableFields(LabWork.class).contains(field)) {
            return repo.getSortedPage(page,field, reversed);
        }
        return null;
    }

    public List<LabWork> getFiltered(Integer page, String field, String filter){
        if (utils.getFilterableFields(LabWork.class).contains(field)) {
            return repo.getFilteredPage(page, field, filter);
        }
        return null;
    }
}
