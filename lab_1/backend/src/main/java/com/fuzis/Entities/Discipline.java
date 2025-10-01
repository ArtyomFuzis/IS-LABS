package com.fuzis.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "discipline", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LabWork> labs;

    public Discipline(String name, Integer labsCount) {
        this.name = name;
        this.labsCount = labsCount;
    }
}
