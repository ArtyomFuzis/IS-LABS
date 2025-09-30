package com.fuzis.Entities;

import com.fuzis.Enums.Color;
import com.fuzis.Enums.Country;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Enumerated(EnumType.STRING)
    @Column(name="EYE_COLOR", columnDefinition = "lab1_color")
    private Color eyeColor;

    @Enumerated(EnumType.STRING)
    @Column(name="HAIR_COLOR", columnDefinition = "lab1_color")
    private Color hairColor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LOCATION_ID")
    private Location location;

    @Column(name="PASSPORT_ID")
    private String passportId;

    @Enumerated(EnumType.STRING)
    @Column(name="NATIONALITY_ID", columnDefinition = "lab1_country")
    private Country nationalityId;

    public Person(String name, String eyeColor, String hairColor, Location location, String passportId, String nationalityId){
        this.name = name;
        this.eyeColor = Color.valueOf(eyeColor);
        this.hairColor = Color.valueOf(hairColor);
        this.location = location;
        this.passportId = passportId;
        this.nationalityId = Country.valueOf(nationalityId);
    }
}
