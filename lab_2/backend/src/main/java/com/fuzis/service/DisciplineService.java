package com.fuzis.service;

import com.fuzis.database.DisciplineRepository;
import com.fuzis.entity.Discipline;
import com.fuzis.util.Locks;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.Collections;
import java.util.List;

@RequestScoped
public class DisciplineService {
    @Inject
    DisciplineRepository repo;

    @Inject
    ValidationService validation;

    @Inject
    UtilityService utils;

    @Inject
    Locks locks;

    public List<Discipline> getAll(){
        return repo.getAll();
    }

    public List<Discipline> getById(Integer id){
        return Collections.singletonList(repo.get(id));
    }

    public void deleteById(Integer id){
        repo.remove(repo.get(id));
    }

    public void create(String name,
                       Integer labs_count,
                       Integer id){
        Discipline discipline;
        synchronized (locks.getLock_insert_update()) {
            if (id != null) {
                discipline = (new Discipline(id, name, labs_count));
                validation.validateForUpdate(discipline);
                repo.merge(discipline);
            } else {
                discipline = (new Discipline(name, labs_count));
                validation.validateForCreate(discipline);
                repo.save(discipline);
            }
        }
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
