package com.fuzis.service;

import com.fuzis.database.LocationRepository;
import com.fuzis.entity.Location;
import com.fuzis.util.Locks;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.Collections;
import java.util.List;

@RequestScoped
public class LocationService {
    @Inject
    LocationRepository repo;

    @Inject
    UtilityService utils;

    @Inject
    ValidationService validation;

    @Inject
    Locks locks;

    public List<Location> getAll(){
        return repo.getAll();
    }

    public List<Location> getById(Integer id){
        return Collections.singletonList(repo.get(id));
    }

    public void deleteById(Integer id){
        repo.remove(repo.get(id));
    }

    public void create(String name,
                       Double x,
                       Double y,
                       Double z,
                       Integer id){
        Location location;
        synchronized (locks.getLock_insert_update()) {
            if (id != null) {
                location = new Location(id, name, x, y, z);
                validation.validateForUpdate(location);
                repo.merge(location);
            } else {
                location = new Location(name, x, y, z);
                validation.validateForCreate(location);
                repo.save(location);
            }
        }
    }

    public List<Location> getPage(Integer page){
        return repo.getPage(page);
    }

    public List<Location> getSorted(Integer page, String field, Boolean reversed){
        if (utils.getFilterableFields(Location.class).contains(field)) {
            return repo.getSortedPage(page,field, reversed);
        }
        return null;
    }

    public List<Location> getFiltered(Integer page, String field, String filter){
        if (utils.getFilterableFields(Location.class).contains(field)) {
            return repo.getFilteredPage(page, field, filter);
        }
        return null;
    }
}
