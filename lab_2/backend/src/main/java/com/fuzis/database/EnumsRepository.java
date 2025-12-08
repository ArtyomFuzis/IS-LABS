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

    public Color findExistingColor(String val) {
        return this.getEntityManager().createQuery(
                        "SELECT c FROM Color c WHERE c.val = :val", Color.class)
                .setParameter("val", val)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public Country findExistingCountry(String val) {
        return this.getEntityManager().createQuery(
                        "SELECT c FROM Country c WHERE c.val = :val", Country.class)
                .setParameter("val", val)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public Difficulty findExistingDifficulty(String val) {
        return this.getEntityManager().createQuery(
                        "SELECT c FROM Difficulty c WHERE c.val = :val", Difficulty.class)
                .setParameter("val", val)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public void persistColor(Color color) {
        this.getEntityManager().persist(color);
    }

    public void persistCountry(Country country) {
        this.getEntityManager().persist(country);
    }

    public void persistDifficulty(Difficulty difficulty) {
        this.getEntityManager().persist(difficulty);
    }

}
