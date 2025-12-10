package com.fuzis.database;

import com.fuzis.entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class YamlReferenceRepository {

    @Inject
    private EntityMangerCreator entityManagerCreator;

    public EntityManager getEntityManager() {
        return entityManagerCreator.getEntityManager();
    }

    public Color findColorById(Integer id) {
        return getEntityManager().find(Color.class, id);
    }

    public Color findColorByValue(String value) {
        return getEntityManager().createQuery(
                        "SELECT c FROM Color c WHERE c.val = :value", Color.class)
                .setParameter("value", value)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public Country findCountryById(Integer id) {
        return getEntityManager().find(Country.class, id);
    }

    public Country findCountryByValue(String value) {
        return getEntityManager().createQuery(
                        "SELECT c FROM Country c WHERE c.val = :value", Country.class)
                .setParameter("value", value)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public Difficulty findDifficultyById(Integer id) {
        return getEntityManager().find(Difficulty.class, id);
    }

    public Difficulty findDifficultyByValue(String value) {
        return getEntityManager().createQuery(
                        "SELECT d FROM Difficulty d WHERE d.val = :value", Difficulty.class)
                .setParameter("value", value)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public Coordinate findCoordinateById(Integer id) {
        return getEntityManager().find(Coordinate.class, id);
    }

    public Location findLocationById(Integer id) {
        return getEntityManager().find(Location.class, id);
    }

    public Person findPersonById(Integer id) {
        return getEntityManager().find(Person.class, id);
    }

    public Discipline findDisciplineById(Integer id) {
        return getEntityManager().find(Discipline.class, id);
    }

    public LabWork findLabWorkById(Integer id) {
        return getEntityManager().find(LabWork.class, id);
    }

    public <T> T findEntityById(Class<T> entityClass, Integer id) {
        return getEntityManager().find(entityClass, id);
    }
}