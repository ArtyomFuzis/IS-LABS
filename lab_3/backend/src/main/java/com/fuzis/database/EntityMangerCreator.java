package com.fuzis.database;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RequestScoped
public class EntityMangerCreator {
    @PersistenceContext(unitName = "postgres")
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }
}