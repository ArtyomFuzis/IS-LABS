package com.fuzis.Services;

import com.fuzis.Entities.Discipline;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;


@ApplicationScoped
public class DBService implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(DBService.class);
    private EntityManager entityManager;

    public EntityManager getEntityManager()
    {
        if (entityManager == null) {
            try {
                entityManager = Persistence.createEntityManagerFactory("default").createEntityManager();
            } catch (Exception e) {
                logger.error("There is an error creating persistence context: {}", String.valueOf(e));
            }
        }
        return entityManager;
    }


    @Transactional
    public void save(Object obj) {
        this.getEntityManager().persist(obj);
    }

    @Transactional
    public void remove(Object obj) {
        this.getEntityManager().remove(obj);
    }

    @Transactional
    public  <T> T get(Integer id,  Class<T> cls) {
        return this.getEntityManager().find(cls, id);
    }

    @Transactional
    public <T> List<T> getAll(Class<T> cls){
        String jpql = "SELECT a FROM "+cls.getName()+" a";
        TypedQuery<T> query = this.getEntityManager().createQuery(jpql, cls);
        return query.getResultList();
    }

}
