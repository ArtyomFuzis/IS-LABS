package com.fuzis.util;

import com.arjuna.ats.jta.TransactionManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


@ApplicationScoped
public class JtaConfiguration {

    private static EntityManagerFactory emf;

    static {
        com.arjuna.ats.jta.TransactionManager.transactionManager();
        emf = Persistence.createEntityManagerFactory("postgres");
    }

    public EntityManagerFactory createEntityManagerFactory() {
        return emf;
    }

    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public EntityManager createEntityManager(EntityManagerFactory emf) {
        return emf.createEntityManager();
    }

    public jakarta.transaction.TransactionManager transactionManager() {
        return TransactionManager.transactionManager();
    }
}