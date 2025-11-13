package com.fuzis.database;

import com.fuzis.entity.Color;
import com.fuzis.entity.Country;
import com.fuzis.entity.Difficulty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class EnumsRepository implements IDatabaseRepository {

    @Inject
    private EntityMangerCreator entityManagerCreator;

    @Override
    public EntityManager getEntityManager() {
        return entityManagerCreator.getEntityManager();
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
