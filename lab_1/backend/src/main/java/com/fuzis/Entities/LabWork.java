package com.fuzis.Entities;

import com.fuzis.Annotations.Filterable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.time.ZonedDateTime;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="LAB1_LABWORK")
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
}
