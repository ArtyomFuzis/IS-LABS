package com.fuzis.service.database;

import com.fuzis.entity.Difficulty;
import com.fuzis.entity.LabWork;
import com.fuzis.entity.Person;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class ExtrasService implements IDatabaseService {
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
