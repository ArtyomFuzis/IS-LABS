package com.fuzis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fuzis.annotation.Filterable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="lab1_coordinate")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, region = "entity")
public class Coordinate {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Filterable
    private Integer id;

    @Column(name="X")
    @Filterable
    private Double x;

    @Column(name="Y")
    @Filterable
    private Double y;

    @JsonIgnore
    @OneToMany(mappedBy = "coordinate", fetch = FetchType.LAZY)
    private List<LabWork> labs;

    public Coordinate(Double x, Double y) {
        this.x = x;
        this.y = y;
        this.labs = new ArrayList<>();
    }

    public Coordinate(Integer id, Double x, Double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.labs = new ArrayList<>();
    }
}
