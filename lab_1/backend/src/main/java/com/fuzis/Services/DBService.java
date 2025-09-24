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
    public void disciplineSave(Discipline point) {
        this.getEntityManager().persist(point);
    }

    @Transactional
    public void disciplineRemove(Discipline point) {
        this.getEntityManager().remove(point);
    }

    @Transactional
    public Discipline disciplineGet(Integer id) {
        return this.getEntityManager().find(Discipline.class, id);
    }

    @Transactional
    public List<Discipline> disciplineGetAll(){
        String jpql = "SELECT a FROM Discipline a";
        TypedQuery<Discipline> query = this.getEntityManager().createQuery(jpql, Discipline.class);
        return query.getResultList();
    }

}
