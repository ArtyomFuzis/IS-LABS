package com.fuzis.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="LAB1_DISCIPLINE")
public class Discipline implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LABS_COUNT")
    private Integer labsCount;

    public Discipline(String name, Integer labsCount) {
        this.name = name;
        this.labsCount = labsCount;
    }
}
