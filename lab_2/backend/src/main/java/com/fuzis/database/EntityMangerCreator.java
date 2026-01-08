package com.fuzis.database;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import static com.fuzis.database.IDatabaseRepository.logger;

@ApplicationScoped
public class EntityMangerCreator {
    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    @ApplicationScoped
    @Produces
    public EntityManager getEntityManager() {
//        if (entityManager == null) {
//            try {
//                entityManager = Persistence.createEntityManagerFactory("default").createEntityManager();
//            } catch (Exception e) {
//                logger.error("There is an error creating persistence context: {}", String.valueOf(e));
//            }
//        }
        return entityManager;
    }
}
