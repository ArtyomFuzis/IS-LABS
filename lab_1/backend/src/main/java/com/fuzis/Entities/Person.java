package com.fuzis.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fuzis.Annotations.Filterable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="lab1_person")
public class Person {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Filterable
    private Integer id;

    @Column(name="NAME")
    @Filterable
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EYE_COLOR")
    @Filterable(addition = ".val")
    private Color eyeColor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HAIR_COLOR")
    @Filterable(addition = ".val")
    private Color hairColor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LOCATION_ID")
    @Filterable(addition = ".id")
    private Location location;

    @Column(name="PASSPORT_ID")
    @Filterable
    private String passportId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "NATIONALITY_ID")
    @Filterable(addition = ".val")
    private Country nationality;

    @JsonIgnore
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<LabWork> labs;

    public Person(String name, Color eyeColor, Color hairColor, Location location, String passportId, Country nationality){
        this.name = name;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.location = location;
        this.passportId = passportId;
        this.nationality = nationality;
        this.labs = new ArrayList<>();
    }

    public Person(Integer id, String name, Color eyeColor, Color hairColor, Location location, String passportId, Country nationality){
        this.id = id;
        this.name = name;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.location = location;
        this.passportId = passportId;
        this.nationality = nationality;
        this.labs = new ArrayList<>();
    }
}
