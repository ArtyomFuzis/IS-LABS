package com.fuzis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "lab1_yaml_import_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class YamlImportHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "time", nullable = false)
    private ZonedDateTime time;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "imported_objects", nullable = false)
    private Integer importedObjects;

    @Column(name = "error_message")
    private String errorMessage;


    public YamlImportHistory(String status, Integer importedObjects) {
        this.status = status;
        this.importedObjects = importedObjects;
    }

    public YamlImportHistory(String status, Integer importedObjects, String errorMessage) {
        this.status = status;
        this.importedObjects = importedObjects;
        this.errorMessage = errorMessage;
    }
}