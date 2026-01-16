package com.fuzis.service;

import com.fuzis.annotation.CacheStatisticsLogging;
import com.fuzis.database.LocationRepository;
import com.fuzis.entity.Location;
import com.fuzis.util.Utils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.List;

@RequestScoped
@CacheStatisticsLogging
public class LocationService {
    @Inject
    LocationRepository repo;

    @Inject
    Utils utils;

    @Inject
    ValidationService validation;

    public List<Location> getAll(){
        return repo.getAll();
    }

    public List<Location> getById(Integer id){
        return Collections.singletonList(repo.get(id));
    }

    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = Exception.class)
    public void deleteById(Integer id){
        repo.remove(repo.get(id));
    }

    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = Exception.class)
    public Integer create(String name,
                       Double x,
                       Double y,
                       Double z,
                       Integer id){
        Location location;

        if (id != null) {
            location = new Location(id, name, x, y, z);
            validation.validateLocation(location);
            repo.merge(location);
        } else {
            location = new Location(name, x, y, z);
            validation.validateLocation(location);
            repo.save(location);
        }
        return location.getId();
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
