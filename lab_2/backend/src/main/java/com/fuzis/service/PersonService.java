package com.fuzis.service;

import com.fuzis.database.PersonRepository;
import com.fuzis.entity.Color;
import com.fuzis.entity.Country;
import com.fuzis.entity.Location;
import com.fuzis.entity.Person;
import com.fuzis.util.Utils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.List;


@RequestScoped
public class PersonService {
    @Inject
    PersonRepository repo;

    @Inject
    Utils utils;

    @Inject
    ValidationService validation;

    public List<Person> getAll(){
        return repo.getAll();
    }

    public List<Person> getById(Integer id){
        return Collections.singletonList(repo.get(id));
    }

    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = Exception.class)
    public void deleteById(Integer id){
        repo.remove(repo.get(id));
    }

    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = Exception.class)
    public Integer create(String name,
                       Integer eyeColorId,
                       Integer hairColorId,
                       Integer locationId,
                       String passportId,
                       Integer nationalityId,
                       Integer id){
        Location location = (locationId != null) ? repo.get(locationId, Location.class) : null;
        Person person;
        if (id != null) {
            person = (new Person(id, name, repo.get(eyeColorId, Color.class), repo.get(hairColorId, Color.class),
                    location, passportId, repo.get(nationalityId, Country.class)));
            validation.validatePerson(person);
            repo.merge(person);
        } else {
            person = (new Person(name, repo.get(eyeColorId, Color.class), repo.get(hairColorId, Color.class),
                    location, passportId, repo.get(nationalityId, Country.class)));
            validation.validatePerson(person);
            repo.save(person);
        }
        return person.getId();

    }

    public List<Person> getPage(Integer page){
        return repo.getPage(page);
    }

    public List<Person> getSorted(Integer page, String field, Boolean reversed){
        if (utils.getFilterableFields(Person.class).contains(field)) {
            return repo.getSortedPage(page,field, reversed);
        }
        return null;
    }

    public List<Person> getFiltered(Integer page, String field, String filter){
        if (utils.getFilterableFields(Person.class).contains(field)) {
            return repo.getFilteredPage(page, field, filter);
        }
        return null;
    }
}
