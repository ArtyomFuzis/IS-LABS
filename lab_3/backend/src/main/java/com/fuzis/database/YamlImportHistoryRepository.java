package com.fuzis.database;

import com.fuzis.entity.YamlImportHistory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class YamlImportHistoryRepository implements IDatabaseRepository {
    @Inject
    private EntityMangerCreator entityManagerCreator;

    @Override
    public EntityManager getEntityManager() {
        return entityManagerCreator.getEntityManager();
    }

    public YamlImportHistory get(Integer id){
        return this.get(id, YamlImportHistory.class);
    }

    public List<YamlImportHistory> getAll(){
        return this.getAll(YamlImportHistory.class);
    }

    public List<YamlImportHistory> getPage(int page) {
        return this.getPage(page, YamlImportHistory.class);
    }

    public List<YamlImportHistory> getFilteredPage(int page, String field, String filter){
        return this.getFilteredPage(page, field, filter, YamlImportHistory.class);
    }

    public List<YamlImportHistory> getSortedPage(int page, String field, boolean reversed){
        return this.getSortedPage(page, field, reversed, YamlImportHistory.class);
    }

    public List<YamlImportHistory> findRecentImports(int limit) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<YamlImportHistory> cq = cb.createQuery(YamlImportHistory.class);
        Root<YamlImportHistory> root = cq.from(YamlImportHistory.class);
        cq.orderBy(cb.desc(root.get("time")));

        TypedQuery<YamlImportHistory> query = em.createQuery(cq);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    public YamlImportHistory findById(Integer id) {
        EntityManager em = getEntityManager();
        return em.find(YamlImportHistory.class, id);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void save(YamlImportHistory history) {
        EntityManager em = getEntityManager();
        if (history.getId() == null) {
            em.persist(history);
        } else {
            em.merge(history);
        }
    }
}