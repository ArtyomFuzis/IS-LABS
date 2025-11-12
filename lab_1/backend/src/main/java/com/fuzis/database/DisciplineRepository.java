package com.fuzis.database;

import com.fuzis.entity.Discipline;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class DisciplineRepository implements IDatabaseRepository {

    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        if (entityManager == null) {
            try {
                entityManager = Persistence.createEntityManagerFactory("default").createEntityManager();
            } catch (Exception e) {
                logger.error("There is an error creating persistence context: {}", String.valueOf(e));
            }
        }
        return entityManager;
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
