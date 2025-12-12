package com.fuzis.database;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import static com.fuzis.database.IDatabaseRepository.logger;

@RequestScoped
public class EntityMangerCreator {
    private EntityManager entityManager;

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
}
