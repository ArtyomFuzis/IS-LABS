package com.fuzis.database;

import com.fuzis.entity.Discipline;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class DisciplineRepository implements IDatabaseRepository {

    @Inject
    private EntityMangerCreator entityManagerCreator;

    @Override
    public EntityManager getEntityManager() {
        return entityManagerCreator.getEntityManager();
    }

    public Discipline get(Integer id){
        return this.get(id, Discipline.class);
    }

    public List<Discipline> getAll(){
        return this.getAll(Discipline.class);
    }

    public List<Discipline> getPage(int page) {
        return this.getPage(page, Discipline.class);
    }

    public List<Discipline> getFilteredPage(int page, String field, String filter){
        return this.getFilteredPage(page, field, filter, Discipline.class);
    }

    public List<Discipline> getSortedPage(int page, String field, boolean reversed){
        return this.getSortedPage(page, field, reversed, Discipline.class);
    }
}
