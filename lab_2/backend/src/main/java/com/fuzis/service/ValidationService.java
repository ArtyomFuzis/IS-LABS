package com.fuzis.service;

import com.fuzis.database.*;
import com.fuzis.entity.*;
import com.fuzis.exception.ValidationException;
import com.fuzis.transferdata.inner.YamlParseResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;

@ApplicationScoped
public class ValidationService {

    @Inject
    private DisciplineRepository disciplineRepository;

    @Inject
    private LabWorkRepository labWorkRepository;

    @Inject
    private CoordinateRepository coordinateRepository;

    // === Методы для валидации отдельных сущностей ===

    // Для Color, Country, Difficulty, Location, Person - пустые методы (без валидации)
    public void validateColor(Color color) {
        // Без валидации
    }

    public void validateCountry(Country country) {
        // Без валидации
    }

    public void validateDifficulty(Difficulty difficulty) {
        // Без валидации
    }

    public void validateCoordinate(Coordinate coordinate) {
        // Без валидации
    }

    public void validateLocation(Location location) {
        // Без валидации
    }

    public void validatePerson(Person person) {
        // Без валидации
    }

    public void validateDiscipline(Discipline discipline) {
        // Без валидации (уникальность проверяется в групповой валидации)
    }

    public void validateLabWork(LabWork labWork) {
        // Только проверка баллов
        List<ValidationException.ValidationError> errors = new ArrayList<>();
        validateLabWorkPoints(labWork, errors);

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

    // === Методы валидации зависимостей ===

    public void validatePersonDependencies(Person person) {
        // Без валидации
    }

    public void validateLabWorkDependencies(LabWork labWork) {
        List<ValidationException.ValidationError> errors = new ArrayList<>();

        // Проверка ограничения по количеству LabWork на дисциплину
        if (labWork.getDiscipline() != null && labWork.getDiscipline().getId() != null) {
            long labWorkCount = countLabWorksByDisciplineId(labWork.getDiscipline().getId());
            if (labWorkCount >= 7) {
                errors.add(new ValidationException.ValidationError("DISCIPLINE_LABWORK_LIMIT",
                        String.format("Discipline '%s' already has %d LabWorks (maximum is 7)",
                                labWork.getDiscipline().getName(), labWorkCount)));
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    // === Методы для комплексной валидации ===

    public void validateYamlResult(YamlParseResult result) {
        List<ValidationException.ValidationError> allErrors = new ArrayList<>();

        // Проверка уникальности имен дисциплин в YAML и БД
        allErrors.addAll(validateUniqueDisciplineNames(result));

        // Проверка уникальности имен LabWork в YAML и БД
        allErrors.addAll(validateUniqueLabWorkNames(result));

        // Проверка уникальности координат в YAML и БД
        allErrors.addAll(validateUniqueCoordinates(result));

        // Проверка баллов LabWork
        allErrors.addAll(validateLabWorkPointsInResult(result));

        // Проверка ограничения по количеству LabWork на дисциплину
        allErrors.addAll(validateDisciplineLabWorkLimits(result));

        if (!allErrors.isEmpty()) {
            throw new ValidationException(allErrors);
        }
    }

    // === Вспомогательные методы для групповой валидации ===

    private List<ValidationException.ValidationError> validateUniqueDisciplineNames(YamlParseResult result) {
        List<ValidationException.ValidationError> errors = new ArrayList<>();
        Set<String> seenNames = new HashSet<>();

        for (Discipline discipline : result.getDisciplines()) {
            String name = discipline.getName();

            if (name == null) continue;

            // Проверка уникальности в рамках YAML
            if (seenNames.contains(name)) {
                errors.add(new ValidationException.ValidationError("DISCIPLINE_NAME_DUPLICATE_YAML",
                        "Discipline name must be unique: '" + name + "' appears multiple times in YAML"));
            } else {
                seenNames.add(name);
            }

            // Проверка уникальности в БД
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

            // Проверка уникальности в рамках YAML
            if (seenNames.contains(name)) {
                errors.add(new ValidationException.ValidationError("LABWORK_NAME_DUPLICATE_YAML",
                        "LabWork name must be unique: '" + name + "' appears multiple times in YAML"));
            } else {
                seenNames.add(name);
            }

            // Проверка уникальности в БД
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

            // Проверка уникальности в рамках YAML
            if (seenCoordinates.contains(coordKey)) {
                errors.add(new ValidationException.ValidationError("COORDINATE_DUPLICATE_YAML",
                        "Coordinate must be unique: " + coordKey + " appears multiple times in YAML"));
            } else {
                seenCoordinates.add(coordKey);
            }

            // Проверка уникальности в БД
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

        // Группируем LabWork по дисциплинам в YAML
        Map<Discipline, Integer> yamlLabWorkCountByDiscipline = new HashMap<>();
        for (LabWork labWork : result.getLabWorks()) {
            if (labWork.getDiscipline() != null) {
                Discipline discipline = labWork.getDiscipline();
                yamlLabWorkCountByDiscipline.merge(discipline, 1, Integer::sum);
            }
        }

        // Для каждой дисциплины проверяем лимит
        for (Map.Entry<Discipline, Integer> entry : yamlLabWorkCountByDiscipline.entrySet()) {
            Discipline discipline = entry.getKey();
            Integer yamlCount = entry.getValue();

            // Если у дисциплины есть ID, проверяем сколько LabWork уже в БД
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

    // === Утилитарные методы для использования в других сервисах ===

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
        // Для update валидация такая же, как для create
        validateForCreate(entity);
    }

    // === Вспомогательные методы для поиска в БД ===

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