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
@Table(name="lab1_coordinate")
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
