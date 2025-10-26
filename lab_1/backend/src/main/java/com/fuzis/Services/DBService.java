package com.fuzis.Services;

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
    public void merge(Object obj) {
        this.getEntityManager().merge(obj);
    }

    @Transactional
    public void remove(Object obj) {
        this.getEntityManager().remove(obj);
    }

    public  <T> T get(Integer id,  Class<T> cls) {
        if(id == null)return null;
        return this.getEntityManager().find(cls, id);
    }

    public <T> List<T> getAll(Class<T> cls){
        String jpql = "SELECT a FROM "+cls.getName()+" a";
        TypedQuery<T> query = this.getEntityManager().createQuery(jpql, cls);
        return query.getResultList();
    }

    public <T> List<T> getPage(int page, int count, Class<T> cls) {
        int offset = (page - 1) * count;

        return this.getEntityManager().createQuery(
                        "SELECT u FROM "+cls.getName()+" u ORDER BY u.id", cls)
                .setFirstResult(offset)
                .setMaxResults(count)
                .getResultList();
    }

    public <T> List<T> getPage(int page, Class<T> cls) {
        return this.getPage(page, 5, cls);
    }

    public <T> List<T> getFilteredPage(int page, int count, String field, String filter, Class<T> cls) {
        int offset = (page - 1) * count;

        return this.getEntityManager().createQuery(
                        "SELECT e FROM "+cls.getName()+" e WHERE CAST(e."+field+" as string) ILIKE :filter", cls)
                .setParameter("filter", "%" + filter + "%")
                .setFirstResult(offset)
                .setMaxResults(count)
                .getResultList();
    }

    public <T> List<T> getSortedPage(int page, int count, String field, boolean reversed, Class<T> cls) {
        int offset = (page - 1) * count;

        return this.getEntityManager().createQuery(
                        "SELECT e FROM "+cls.getName()+" e ORDER BY e."+field+" "+(reversed ? "DESC" : "ASC"), cls)
                .setFirstResult(offset)
                .setMaxResults(count)
                .getResultList();
    }

    public <T> List<T> getFilteredPage(int page, String field, String filter, Class<T> cls) {
        return this.getFilteredPage(page, 5, field, filter, cls);
    }

    public <T> List<T> getSortedPage(int page, String field, boolean reversed, Class<T> cls) {
        return this.getSortedPage(page,5,field, reversed, cls);
    }
}