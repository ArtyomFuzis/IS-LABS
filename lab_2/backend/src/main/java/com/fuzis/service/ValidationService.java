package com.fuzis.service;

import com.fuzis.database.*;
import com.fuzis.entity.*;
import com.fuzis.exception.ValidationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ValidationService {

    @Inject
    private DisciplineRepository disciplineRepository;

    @Inject
    private LabWorkRepository labWorkRepository;

    @Inject
    private CoordinateRepository coordinateRepository;

    @Inject
    private PersonRepository personRepository;

    public void validateColor(Color color) {
    }

    public void validateCountry(Country country) {
    }

    public void validateDifficulty(Difficulty difficulty) {
    }

    public void validateLocation(Location location) {
    }

    // Методы со сложной валидацией
    public void validateCoordinate(Coordinate coordinate) {
        validateCoordinateUnique(coordinate);
    }

    public void validatePerson(Person person) {
        validatePersonUnique(person);
    }

    public void validateDiscipline(Discipline discipline) {
        validateDisciplineUnique(discipline);
    }

    public void validateLabWork(LabWork labWork) {
        validateLabWorkPoints(labWork);
        validateLabWorkUnique(labWork);
        validateDisciplineLabWorkLimit(labWork);
        validateGeographicConsistency(labWork);
    }

    private void validateLabWorkPoints(LabWork labWork) {
        Double minimalPoint = labWork.getMinimalPoint();
        Double maximalPoint = labWork.getMaximalPoint();
        if (minimalPoint == null) {
            throw new ValidationException("Minimal Point can't be null");
        }
        if (maximalPoint != null) {
            if (minimalPoint > maximalPoint) {
                throw new ValidationException(String.format("LabWork minimalPoint (%.2f) must be <= maximalPoint (%.2f)", minimalPoint, maximalPoint));
            }
            if (maximalPoint > 100) {
                throw new ValidationException(String.format("LabWork maximalPoint (%.2f) must be <= 100", maximalPoint));
            }
        }
    }

    private void validateDisciplineLabWorkLimit(LabWork labWork) {
        if (labWork.getDiscipline() != null) {
            long labWorkCount = labWorkRepository.countByDisciplineId(labWork.getDiscipline().getId());
            if (labWorkCount >= 7) {
                throw new ValidationException(String.format("Discipline '%s' already has %d LabWorks (maximum is 7)", labWork.getDiscipline().getName(), labWorkCount));
            }
        }
    }

    private void validateGeographicConsistency(LabWork labWork) {
        Person author = labWork.getAuthor();
        if (author == null) return;

        Location authorLocation = author.getLocation();
        if (authorLocation == null) return;

        Coordinate labWorkCoordinate = labWork.getCoordinate();
        if (labWorkCoordinate == null) return;

        double distance = calculateDistance(authorLocation.getX(), authorLocation.getY(), labWorkCoordinate.getX(), labWorkCoordinate.getY());

        if (distance > 1000.0) {
            throw new ValidationException(String.format("Distance between author's location (%.2f, %.2f) " + "and LabWork coordinate (%.2f, %.2f) is %.2f, which exceeds 1000", authorLocation.getX(), authorLocation.getY(), labWorkCoordinate.getX(), labWorkCoordinate.getY(), distance));
        }
    }

    private void validateDisciplineUnique(Discipline discipline) {
        if (discipline.getName() == null) return;

        Discipline existing = disciplineRepository.findByName(discipline.getName());
        if (existing != null && !existing.getId().equals(discipline.getId())) {

            throw new ValidationException("Discipline name already exists: '" + discipline.getName() + "'");
        }
    }

    private void validateLabWorkUnique(LabWork labWork) {
        if (labWork.getName() == null) return;

        LabWork existing = labWorkRepository.findByName(labWork.getName());
        if (existing != null && !existing.getId().equals(labWork.getId())) {
            throw new ValidationException(

                    "LabWork name already exists: '" + labWork.getName() + "'");
        }
    }

    private void validateCoordinateUnique(Coordinate coordinate) {
        Double x = coordinate.getX();
        Double y = coordinate.getY();

        if (x == null || y == null) return;

        Coordinate existing = coordinateRepository.findByXAndY(x, y);
        if (existing != null && !existing.getId().equals(coordinate.getId())) {
            throw new ValidationException(

                    String.format("Coordinate already exists: (%.2f, %.2f)", x, y));
        }
    }

    private void validatePersonUnique(Person person) {
        if (person.getPassportId() == null) return;

        Person existing = personRepository.findByPassportId(person.getPassportId());
        if (existing != null && !existing.getId().equals(person.getId())) {
            throw new ValidationException("Person with passport ID already exists: '" + person.getPassportId() + "'");
        }
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}