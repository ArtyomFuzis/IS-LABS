package com.fuzis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fuzis.annotation.Filterable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="lab1_discipline")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, region = "entity")
public class Discipline implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Filterable
    private Integer id;

    @Column(name = "NAME")
    @Filterable
    private String name;

    @Column(name = "LABS_COUNT")
    @Filterable
    private Integer labsCount;

    @JsonIgnore
    @OneToMany(mappedBy = "discipline", fetch = FetchType.LAZY)
    private List<LabWork> labs;

    public Discipline(String name, Integer labsCount) {
        this.name = name;
        this.labsCount = labsCount;
        this.labs = new ArrayList<>();
    }

    public Discipline(Integer id, String name, Integer labsCount) {
        this.id = id;
        this.name = name;
        this.labsCount = labsCount;
        this.labs = new ArrayList<>();
    }
}
