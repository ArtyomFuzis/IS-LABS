package com.fuzis.database;

import com.fuzis.util.JtaConfiguration;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class EntityMangerCreator {
    @Inject
    private JtaConfiguration jtaConfiguration;

    private static final Logger logger = LoggerFactory.getLogger(EntityMangerCreator.class);

    private EntityManager em;

    public EntityManager getEntityManager() {
        try {
            if(em == null) {
                em = jtaConfiguration.createEntityManager();
            }
            return em;
        } catch (Exception e) {
            logger.error("Error creating EntityManager: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create EntityManager", e);
        }
    }
}