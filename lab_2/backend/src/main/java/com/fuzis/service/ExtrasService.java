package com.fuzis.service;

import com.fuzis.database.ExtrasRepository;
import com.fuzis.entity.LabWork;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequestScoped
public class ExtrasService
{
    @Inject
    ExtrasRepository repo;

    @Transactional
    public void deleteAllLabsByAuthorId(Integer id){
        var author = repo.getPerson(id);
        System.out.println("Found size: " + author.getLabs().size());
        author.getLabs().forEach(lab -> repo.remove(lab));
    }

    public Double getMaximumPointSum(){
        List<LabWork> labWorks = repo.getAllLabWorks();
        if (labWorks.isEmpty()) return 0.0;
        return labWorks
                .stream()
                .map(LabWork::getMaximalPoint)
                .reduce(0.0d, (a, b) -> ((a == null ? 0.0 : a) + (b == null ? 0.0 : b)));
    }

    public List<Double> getMinimalPointUnique(){
        return repo
                .getAllLabWorks()
                .stream()
                .map(LabWork::getMinimalPoint)
                .collect(Collectors.toUnmodifiableSet())
                .stream().toList();
    }

    @Transactional
    public boolean postIncreaseDifficulty(Integer id, Integer steps){
        var lab = repo.getLabWork(id);
        var new_difficulty = repo.getDifficulty(lab.getDifficulty().getId()+steps);
        if(new_difficulty == null) {
            return false;
        }
        else{
            lab.setDifficulty(new_difficulty);
            repo.save(lab);
            return true;
        }
    }

    @Transactional
    public void deleteLabFromDiscipline(Integer id){
        var lab = repo.getLabWork(id);
        lab.setDiscipline(null);
        repo.merge(lab);
    }
}
