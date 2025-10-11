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
@Table(name="LAB1_LOCATION")
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
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Person> people;

    public Location(String name, Double x, Double y, Double z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }
}
