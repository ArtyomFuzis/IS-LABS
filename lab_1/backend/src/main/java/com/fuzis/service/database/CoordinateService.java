package com.fuzis.service.database;

import com.fuzis.entity.Coordinate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class CoordinateService implements IDatabaseService {

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

    public Coordinate get(Integer id){
        return this.get(id, Coordinate.class);
    }

    public List<Coordinate> getAll(){
        return this.getAll(Coordinate.class);
    }

    public List<Coordinate> getPage(int page) {
        return this.getPage(page, Coordinate.class);
    }

    public List<Coordinate> getFilteredPage(int page, String field, String filter){
        return this.getFilteredPage(page, field, filter, Coordinate.class);
    }

    public List<Coordinate> getSortedPage(int page, String field, boolean reversed){
        return this.getSortedPage(page, field, reversed, Coordinate.class);
    }
}
