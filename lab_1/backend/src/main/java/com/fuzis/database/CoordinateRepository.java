package com.fuzis.database;

import com.fuzis.entity.Coordinate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class CoordinateRepository implements IDatabaseRepository {

    @Inject
    private EntityMangerCreator entityManagerCreator;

    @Override
    public EntityManager getEntityManager() {
        return entityManagerCreator.getEntityManager();
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
