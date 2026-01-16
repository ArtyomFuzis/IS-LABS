package com.fuzis.database;

import com.fuzis.entity.Difficulty;
import com.fuzis.entity.LabWork;
import com.fuzis.entity.Person;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@RequestScoped
public class ExtrasRepository implements IDatabaseRepository {

    @Inject
    private EntityMangerCreator entityManagerCreator;

    @Override
    public EntityManager getEntityManager() {
        return entityManagerCreator.getEntityManager();
    }

    public List<LabWork> getAllLabWorks(){
        return this.getAll(LabWork.class);
    }

    public Person getPerson(Integer id){
        return this.get(id, Person.class);
    }

    public LabWork getLabWork(Integer id){
        return this.get(id, LabWork.class);
    }

    public Difficulty getDifficulty(Integer id){
        return this.get(id, Difficulty.class);
    }
}
