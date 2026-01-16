package com.fuzis.entity;

import com.fuzis.annotation.Filterable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.ZonedDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name="lab1_labwork")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, region = "entity")
public class LabWork {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Filterable
    private Integer id;

    @Column(name = "NAME")
    @Filterable
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COORDINATES_ID")
    @Filterable(addition = ".id")
    private Coordinate coordinate;

    @Column(name = "CREATION_DATE")
    @Filterable
    private ZonedDateTime creationDate;

    @Column(name = "DESCRIPTION")
    @Filterable
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DIFFICULTY_ID")
    @Filterable(addition = ".val")
    private Difficulty difficulty;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DISCIPLINE_ID")
    @Filterable(addition = ".id")
    private Discipline discipline;

    @Column(name = "MINIMAL_POINT")
    @Filterable
    private Double minimalPoint;

    @Column(name = "MAXIMUM_POINT")
    @Filterable
    private Double maximalPoint;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AUTHOR_ID")
    @Filterable(addition = ".id")
    private Person author;

    public LabWork(String name, Coordinate coordinate, ZonedDateTime creationDate, String description, Difficulty difficulty,  Discipline discipline, Double minimalPoint, Double maximalPoint, Person author) {
        this.name = name;
        this.coordinate = coordinate;
        this.difficulty = difficulty;
        this.discipline = discipline;
        this.minimalPoint = minimalPoint;
        this.maximalPoint = maximalPoint;
        this.author = author;
        this.creationDate = creationDate;
        this.description = description;
    }

    public LabWork(Integer id, String name, Coordinate coordinate, ZonedDateTime creationDate, String description, Difficulty difficulty,  Discipline discipline, Double minimalPoint, Double maximalPoint, Person author) {
        this.id = id;
        this.name = name;
        this.coordinate = coordinate;
        this.difficulty = difficulty;
        this.discipline = discipline;
        this.minimalPoint = minimalPoint;
        this.maximalPoint = maximalPoint;
        this.author = author;
        this.creationDate = creationDate;
        this.description = description;
    }
}
