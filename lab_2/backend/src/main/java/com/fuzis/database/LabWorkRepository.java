package com.fuzis.database;

import com.fuzis.entity.LabWork;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@ApplicationScoped
public class LabWorkRepository implements IDatabaseRepository {

    @Inject
    private EntityMangerCreator entityManagerCreator;

    @Override
    public EntityManager getEntityManager() {
        return entityManagerCreator.getEntityManager();
    }

    public LabWork get(Integer id){
        return this.get(id, LabWork.class);
    }

    public List<LabWork> getAll(){
        return this.getAll(LabWork.class);
    }

    public List<LabWork> getPage(int page) {
        return this.getPage(page, LabWork.class);
    }

    public List<LabWork> getFilteredPage(int page, String field, String filter){
        return this.getFilteredPage(page, field, filter, LabWork.class);
    }

    public List<LabWork> getSortedPage(int page, String field, boolean reversed){
        return this.getSortedPage(page, field, reversed, LabWork.class);
    }

    public LabWork findByName(String name) {
        if (name == null) return null;

        TypedQuery<LabWork> query = getEntityManager().createQuery(
                "SELECT l FROM LabWork l WHERE l.name = :name", LabWork.class);
        query.setParameter("name", name);

        return query.getResultStream().findFirst().orElse(null);
    }

    public long countByDisciplineId(Integer disciplineId) {
        if (disciplineId == null) return 0;

        TypedQuery<Long> query = getEntityManager().createQuery(
                "SELECT COUNT(l) FROM LabWork l WHERE l.discipline.id = :disciplineId", Long.class);
        query.setParameter("disciplineId", disciplineId);

        return query.getSingleResult();
    }

}
