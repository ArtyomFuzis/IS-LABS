package com.fuzis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fuzis.annotation.Filterable;
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
@Table(name="lab1_location")
public class Location {
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

    @Column(name="Z")
    @Filterable
    private Double z;

    @Column(name="NAME")
    @Filterable
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private List<Person> people;

    public Location(String name, Double x, Double y, Double z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
        this.people = new ArrayList<>();
    }
    public Location(Integer id, String name, Double x, Double y, Double z){
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
        this.people = new ArrayList<>();
    }
}
