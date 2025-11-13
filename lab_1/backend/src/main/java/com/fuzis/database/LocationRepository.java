package com.fuzis.database;

import com.fuzis.entity.Location;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class LocationRepository implements IDatabaseRepository {

    @Inject
    private EntityMangerCreator entityManagerCreator;

    @Override
    public EntityManager getEntityManager() {
        return entityManagerCreator.getEntityManager();
    }

    public Location get(Integer id){
        return this.get(id, Location.class);
    }

    public List<Location> getAll(){
        return this.getAll(Location.class);
    }

    public List<Location> getPage(int page) {
        return this.getPage(page, Location.class);
    }

    public List<Location> getFilteredPage(int page, String field, String filter){
        return this.getFilteredPage(page, field, filter, Location.class);
    }

    public List<Location> getSortedPage(int page, String field, boolean reversed){
        return this.getSortedPage(page, field, reversed, Location.class);
    }
}
