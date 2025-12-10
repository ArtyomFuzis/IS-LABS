package com.fuzis.service;

import com.fuzis.database.*;
import com.fuzis.entity.*;
import com.fuzis.transferdata.inner.YamlParseResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.fuzis.exception.ValidationException;

import java.util.*;

@ApplicationScoped
public class ValidationService {

    @Inject
    private DisciplineRepository disciplineRepository;

    @Inject
    private LabWorkRepository labWorkRepository;

    @Inject
    private CoordinateRepository coordinateRepository;

    public void validateColor(Color color) {
        
    }

    public void validateCountry(Country country) {
        
    }

    public void validateDifficulty(Difficulty difficulty) {
        
    }

    public void validateCoordinate(Coordinate coordinate) {
        List<ValidationException.ValidationError> errors = new ArrayList<>();
        validateCoordinateUnique(coordinate, errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateLocation(Location location) {
        
    }

    public void validatePerson(Person person) {
        
    }

    public void validateDiscipline(Discipline discipline) {
        List<ValidationException.ValidationError> errors = new ArrayList<>();
        validateDisciplineUnique(discipline, errors);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateLabWork(LabWork labWork) {
        List<ValidationException.ValidationError> errors = new ArrayList<>();
        validateLabWorkPoints(labWork, errors);
        validateLabWorkUnique(labWork, errors);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validateLabWorkPoints(LabWork labWork, List<ValidationException.ValidationError> errors) {
        Double minimalPoint = labWork.getMinimalPoint();
        Double maximalPoint = labWork.getMaximalPoint();

        if (minimalPoint != null && maximalPoint != null) {
            if (minimalPoint <= 0) {
                errors.add(new ValidationException.ValidationError("LABWORK_MINIMAL_POINT_INVALID",
                        "LabWork minimalPoint must be greater than 0"));
            }

            if (minimalPoint > maximalPoint) {
                errors.add(new ValidationException.ValidationError("LABWORK_POINTS_INVALID",
                        String.format("LabWork minimalPoint (%.2f) must be <= maximalPoint (%.2f)",
                                minimalPoint, maximalPoint)));
            }

            if (maximalPoint > 100) {
                errors.add(new ValidationException.ValidationError("LABWORK_MAXIMAL_POINT_INVALID",
                        String.format("LabWork maximalPoint (%.2f) must be <= 100", maximalPoint)));
            }
        } else if (minimalPoint != null && minimalPoint <= 0) {
            errors.add(new ValidationException.ValidationError("LABWORK_MINIMAL_POINT_INVALID",
                    "LabWork minimalPoint must be greater than 0"));
        } else if (maximalPoint != null && maximalPoint > 100) {
            errors.add(new ValidationException.ValidationError("LABWORK_MAXIMAL_POINT_INVALID",
                    String.format("LabWork maximalPoint (%.2f) must be <= 100", maximalPoint)));
        }
    }

    public void validatePersonDependencies(Person person) {
        
    }

    public void validateLabWorkDependencies(LabWork labWork) {
        List<ValidationException.ValidationError> errors = new ArrayList<>();
        validateDisciplineLabWorkLimit(labWork, errors);
        validateGeographicConsistency(labWork, errors);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validateDisciplineLabWorkLimit(LabWork labWork, List<ValidationException.ValidationError> errors) {
        if (labWork.getDiscipline() != null && labWork.getDiscipline().getId() != null) {
            long labWorkCount = countLabWorksByDisciplineId(labWork.getDiscipline().getId());
            if (labWorkCount >= 7) {
                errors.add(new ValidationException.ValidationError("DISCIPLINE_LABWORK_LIMIT",
                        String.format("Discipline '%s' already has %d LabWorks (maximum is 7)",
                                labWork.getDiscipline().getName(), labWorkCount)));
            }
        }
    }

    private void validateGeographicConsistency(LabWork labWork, List<ValidationException.ValidationError> errors) {
        Person author = labWork.getAuthor();
        if (author == null) {
            
            return;
        }
        Location authorLocation = author.getLocation();
        if (authorLocation == null) {
            
            return;
        }

        Coordinate labWorkCoordinate = labWork.getCoordinate();
        if (labWorkCoordinate == null) {
            
            return;
        }
        
        double distance = calculateDistance(
                authorLocation.getX(), authorLocation.getY(),
                labWorkCoordinate.getX(), labWorkCoordinate.getY()
        );
        
        if (distance > 1000.0) {
            errors.add(new ValidationException.ValidationError("GEOGRAPHIC_CONSISTENCY",
                    String.format("Distance between author's location (%.2f, %.2f) " +
                                    "and LabWork coordinate (%.2f, %.2f) is %.2f, which exceeds 1000",
                            authorLocation.getX(), authorLocation.getY(),
                            labWorkCoordinate.getX(), labWorkCoordinate.getY(),
                            distance)));
        }
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public void validateYamlResult(YamlParseResult result) {
        List<ValidationException.ValidationError> allErrors = new ArrayList<>();

        allErrors.addAll(validateUniqueDisciplineNames(result));
        allErrors.addAll(validateUniqueLabWorkNames(result));
        allErrors.addAll(validateUniqueCoordinates(result));
        allErrors.addAll(validateLabWorkPointsInResult(result));
        allErrors.addAll(validateDisciplineLabWorkLimits(result));
        allErrors.addAll(validateGeographicConsistencyInResult(result));
        if (!allErrors.isEmpty()) {
            throw new ValidationException(allErrors);
        }
    }

    private List<ValidationException.ValidationError> validateGeographicConsistencyInResult(YamlParseResult result) {
        List<ValidationException.ValidationError> errors = new ArrayList<>();

        for (LabWork labWork : result.getLabWorks()) {
            
            List<ValidationException.ValidationError> labWorkErrors = new ArrayList<>();
            validateGeographicConsistency(labWork, labWorkErrors);

            if (!labWorkErrors.isEmpty()) {
                
                for (ValidationException.ValidationError error : labWorkErrors) {
                    errors.add(new ValidationException.ValidationError(
                            error.getErrorCode(),
                            String.format("LabWork '%s': %s",
                                    labWork.getName() != null ? labWork.getName() : "Unnamed",
                                    error.getMessage())
                    ));
                }
            }
        }

        return errors;
    }

    

    private void validateDisciplineUnique(Discipline discipline, List<ValidationException.ValidationError> errors) {
        if (discipline.getName() == null) return;

        Discipline existing = findDisciplineByName(discipline.getName());
        if (existing != null && !Objects.equals(existing.getId(), discipline.getId())) {
            errors.add(new ValidationException.ValidationError("DISCIPLINE_NAME_DUPLICATE_DB",
                    "Discipline name already exists in database: '" + discipline.getName() + "'"));
        }
    }

    private void validateLabWorkUnique(LabWork labWork, List<ValidationException.ValidationError> errors) {
        if (labWork.getName() == null) return;

        LabWork existing = findLabWorkByName(labWork.getName());
        if (existing != null && !Objects.equals(existing.getId(), labWork.getId())) {
            errors.add(new ValidationException.ValidationError("LABWORK_NAME_DUPLICATE_DB",
                    "LabWork name already exists in database: '" + labWork.getName() + "'"));
        }
    }

    private void validateCoordinateUnique(Coordinate coordinate, List<ValidationException.ValidationError> errors) {
        Double x = coordinate.getX();
        Double y = coordinate.getY();

        if (x == null || y == null) return;

        Coordinate existing = findCoordinateByXAndY(x, y);
        if (existing != null && !Objects.equals(existing.getId(), coordinate.getId())) {
            errors.add(new ValidationException.ValidationError("COORDINATE_DUPLICATE_DB",
                    String.format("Coordinate already exists in database: (%.2f, %.2f)", x, y)));
        }
    }

    

    private List<ValidationException.ValidationError> validateUniqueDisciplineNames(YamlParseResult result) {
        List<ValidationException.ValidationError> errors = new ArrayList<>();
        Set<String> seenNames = new HashSet<>();

        for (Discipline discipline : result.getDisciplines()) {
            String name = discipline.getName();

            if (name == null) continue;

            if (seenNames.contains(name)) {
                errors.add(new ValidationException.ValidationError("DISCIPLINE_NAME_DUPLICATE_YAML",
                        "Discipline name must be unique: '" + name + "' appears multiple times in YAML"));
            } else {
                seenNames.add(name);
            }

            Discipline existing = findDisciplineByName(name);
            if (existing != null) {
                errors.add(new ValidationException.ValidationError("DISCIPLINE_NAME_DUPLICATE_DB",
                        "Discipline name already exists in database: '" + name + "'"));
            }
        }

        return errors;
    }

    private List<ValidationException.ValidationError> validateUniqueLabWorkNames(YamlParseResult result) {
        List<ValidationException.ValidationError> errors = new ArrayList<>();
        Set<String> seenNames = new HashSet<>();

        for (LabWork labWork : result.getLabWorks()) {
            String name = labWork.getName();

            if (name == null) continue;

            if (seenNames.contains(name)) {
                errors.add(new ValidationException.ValidationError("LABWORK_NAME_DUPLICATE_YAML",
                        "LabWork name must be unique: '" + name + "' appears multiple times in YAML"));
            } else {
                seenNames.add(name);
            }

            LabWork existing = findLabWorkByName(name);
            if (existing != null) {
                errors.add(new ValidationException.ValidationError("LABWORK_NAME_DUPLICATE_DB",
                        "LabWork name already exists in database: '" + name + "'"));
            }
        }

        return errors;
    }

    private List<ValidationException.ValidationError> validateUniqueCoordinates(YamlParseResult result) {
        List<ValidationException.ValidationError> errors = new ArrayList<>();
        Set<String> seenCoordinates = new HashSet<>();

        for (Coordinate coordinate : result.getCoordinates()) {
            Double x = coordinate.getX();
            Double y = coordinate.getY();

            if (x == null || y == null) continue;

            String coordKey = String.format("(%.2f, %.2f)", x, y);

            if (seenCoordinates.contains(coordKey)) {
                errors.add(new ValidationException.ValidationError("COORDINATE_DUPLICATE_YAML",
                        "Coordinate must be unique: " + coordKey + " appears multiple times in YAML"));
            } else {
                seenCoordinates.add(coordKey);
            }

            Coordinate existing = findCoordinateByXAndY(x, y);
            if (existing != null) {
                errors.add(new ValidationException.ValidationError("COORDINATE_DUPLICATE_DB",
                        "Coordinate already exists in database: " + coordKey));
            }
        }

        return errors;
    }

    private List<ValidationException.ValidationError> validateLabWorkPointsInResult(YamlParseResult result) {
        List<ValidationException.ValidationError> errors = new ArrayList<>();

        for (LabWork labWork : result.getLabWorks()) {
            try {
                validateLabWork(labWork);
            } catch (ValidationException e) {
                errors.addAll(e.getValidationErrors());
            }
        }

        return errors;
    }

    private List<ValidationException.ValidationError> validateDisciplineLabWorkLimits(YamlParseResult result) {
        List<ValidationException.ValidationError> errors = new ArrayList<>();

        
        Map<Discipline, Integer> yamlLabWorkCountByDiscipline = new HashMap<>();
        for (LabWork labWork : result.getLabWorks()) {
            if (labWork.getDiscipline() != null) {
                Discipline discipline = labWork.getDiscipline();
                yamlLabWorkCountByDiscipline.merge(discipline, 1, Integer::sum);
            }
        }

        for (Map.Entry<Discipline, Integer> entry : yamlLabWorkCountByDiscipline.entrySet()) {
            Discipline discipline = entry.getKey();
            Integer yamlCount = entry.getValue();

            
            if (discipline.getId() != null) {
                long dbCount = countLabWorksByDisciplineId(discipline.getId());
                long totalCount = dbCount + yamlCount;

                if (totalCount > 7) {
                    errors.add(new ValidationException.ValidationError("DISCIPLINE_LABWORK_LIMIT_YAML",
                            String.format("Discipline '%s' would have %d LabWorks (maximum is 7). " +
                                            "Currently %d in DB + %d new in YAML",
                                    discipline.getName(), totalCount, dbCount, yamlCount)));
                }
            }
        }

        return errors;
    }

    

    public void validateForCreate(Object entity) {
        if (entity instanceof Color) {
            validateColor((Color) entity);
        } else if (entity instanceof Country) {
            validateCountry((Country) entity);
        } else if (entity instanceof Difficulty) {
            validateDifficulty((Difficulty) entity);
        } else if (entity instanceof Coordinate) {
            validateCoordinate((Coordinate) entity);
        } else if (entity instanceof Location) {
            validateLocation((Location) entity);
        } else if (entity instanceof Person) {
            validatePerson((Person) entity);
        } else if (entity instanceof Discipline) {
            validateDiscipline((Discipline) entity);
        } else if (entity instanceof LabWork) {
            validateLabWork((LabWork) entity);
            validateLabWorkDependencies((LabWork) entity);
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + entity.getClass());
        }
    }

    public void validateForUpdate(Object entity) {
        
        validateForCreate(entity);
    }

    

    private Discipline findDisciplineByName(String name) {
        if (name == null) return null;
        return disciplineRepository.findByName(name);
    }

    private LabWork findLabWorkByName(String name) {
        if (name == null) return null;
        return labWorkRepository.findByName(name);
    }

    private Coordinate findCoordinateByXAndY(Double x, Double y) {
        if (x == null || y == null) return null;
        return coordinateRepository.findByXAndY(x, y);
    }

    private long countLabWorksByDisciplineId(Integer disciplineId) {
        if (disciplineId == null) return 0;
        return labWorkRepository.countByDisciplineId(disciplineId);
    }
}