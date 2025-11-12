package com.fuzis.service.database;

import com.fuzis.entity.LabWork;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class LabWorkService implements IDatabaseService {

    private EntityManager entityManager;

    @Override
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
}
