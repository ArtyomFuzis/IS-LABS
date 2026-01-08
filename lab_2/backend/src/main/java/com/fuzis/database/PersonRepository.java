package com.fuzis.database;

import com.fuzis.entity.LabWork;
import com.fuzis.entity.Person;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@ApplicationScoped
public class PersonRepository implements IDatabaseRepository {

    @Inject
    private EntityMangerCreator entityManagerCreator;

    @Override
    public EntityManager getEntityManager() {
        return entityManagerCreator.getEntityManager();
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

    public Person findByPassportId(String passportId) {
        if (passportId == null) return null;

        TypedQuery<Person> query = getEntityManager().createQuery(
                "SELECT p FROM Person p WHERE p.passportId = :passportId", Person.class);
        query.setParameter("passportId", passportId);

        return query.getResultStream().findFirst().orElse(null);
    }
}
