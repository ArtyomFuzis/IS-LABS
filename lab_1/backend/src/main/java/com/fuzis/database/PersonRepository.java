package com.fuzis.service.database;

import com.fuzis.entity.Person;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class PersonService implements IDatabaseService {

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

    public Person get(Integer id){
        return this.get(id, Person.class);
    }

    public List<Person> getAll(){
        return this.getAll(Person.class);
    }

    public List<Person> getPage(int page) {
        return this.getPage(page, Person.class);
    }

    public List<Person> getFilteredPage(int page, String field, String filter){
        return this.getFilteredPage(page, field, filter, Person.class);
    }

    public List<Person> getSortedPage(int page, String field, boolean reversed){
        return this.getSortedPage(page, field, reversed, Person.class);
    }
}
