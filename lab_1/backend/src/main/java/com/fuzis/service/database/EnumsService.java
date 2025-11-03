package com.fuzis.service.database;

import com.fuzis.entity.Color;
import com.fuzis.entity.Country;
import com.fuzis.entity.Difficulty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class EnumsService implements IDatabaseService {
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

    public List<Color> getColorVals(){
        return this.getAll(Color.class);
    }

    public List<Country> getCountryVals(){
        return this.getAll(Country.class);
    }

    public List<Difficulty> getDifficultyVals(){
        return this.getAll(Difficulty.class);
    }
}
