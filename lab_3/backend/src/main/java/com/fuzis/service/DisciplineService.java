package com.fuzis.service;

import com.fuzis.annotation.CacheStatisticsLogging;
import com.fuzis.database.DisciplineRepository;
import com.fuzis.entity.Discipline;
import com.fuzis.util.Utils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
@CacheStatisticsLogging
public class DisciplineService {
    @Inject
    DisciplineRepository repo;

    @Inject
    ValidationService validation;

    @Inject
    Utils utils;

    public List<Discipline> getAll(){
        return repo.getAll();
    }

    public List<Discipline> getById(Integer id){
        return Collections.singletonList(repo.get(id));
    }

    @Transactional
    public void deleteById(Integer id){
        repo.remove(repo.get(id));
    }

    @Transactional
    public Integer create(String name,
                       Integer labs_count,
                       Integer id){
        Discipline discipline;
        if (id != null) {
            discipline = (new Discipline(id, name, labs_count));
            validation.validateDiscipline(discipline);
            repo.merge(discipline);
        } else {
            discipline = (new Discipline(name, labs_count));
            validation.validateDiscipline(discipline);
            repo.save(discipline);
        }
        return discipline.getId();
    }

    public List<Discipline> getPage(Integer page){
        return repo.getPage(page);
    }

    public List<Discipline> getSorted(Integer page, String field, Boolean reversed){
        if (utils.getFilterableFields(Discipline.class).contains(field)) {
            return repo.getSortedPage(page,field, reversed);
        }
        return null;
    }

    public List<Discipline> getFiltered(Integer page, String field, String filter){
        if (utils.getFilterableFields(Discipline.class).contains(field)) {
            return repo.getFilteredPage(page, field, filter);
        }
        return null;
    }
}
