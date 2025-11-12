package com.fuzis.service.database;

import com.fuzis.entity.Location;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class LocationService implements IDatabaseService {

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
