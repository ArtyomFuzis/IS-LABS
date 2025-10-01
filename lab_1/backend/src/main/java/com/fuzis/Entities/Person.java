package com.fuzis.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name="LAB1_PERSON")
public class Person {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="NAME")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EYE_COLOR")
    private Color eyeColor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HAIR_COLOR")
    private Color hairColor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LOCATION_ID")
    private Location location;

    @Column(name="PASSPORT_ID")
    private String passportId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "NATIONALITY_ID")
    private Country nationality;

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LabWork> labs;

    public Person(String name, Color eyeColor, Color hairColor, Location location, String passportId, Country nationality){
        this.name = name;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.location = location;
        this.passportId = passportId;
        this.nationality = nationality;
    }
}
