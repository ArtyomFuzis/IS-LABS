package com.fuzis.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="LAB1_COORDINATES")
public class Coordinate {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="X")
    private Double x;

    @Column(name="Y")
    private Double y;

    @JsonIgnore
    @OneToMany(mappedBy = "coordinate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LabWork> labs;

    public Coordinate(Double x, Double y) {
        this.x = x;
        this.y = y;
    }
}
