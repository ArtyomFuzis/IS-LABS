package com.fuzis.service;

import com.fuzis.database.*;
import com.fuzis.entity.*;
import com.fuzis.exception.ValidationException;
import com.fuzis.exception.YamlSyntaxException;
import com.fuzis.transferdata.inner.YamlParseResult;
import com.fuzis.util.YamlParserBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@RequestScoped
public class YamlImportService {

    @Inject
    private YamlParserBean yamlParser;

    @Inject
    private ValidationService validationService;

    @Inject
    private EnumsRepository enumsRepository;

    @Inject
    private LabWorkRepository labWorkRepository;

    @Inject
    private PersonRepository personRepository;

    @Inject
    private LocationRepository locationRepository;

    @Inject
    private CoordinateRepository coordinateRepository;

    @Inject
    private DisciplineRepository disciplineRepository;

    @Inject
    private YamlImportHistoryRepository importHistoryRepository;

    public YamlParseResult parseYaml(InputStream yamlStream) {
        return yamlParser.parseYaml(yamlStream);
    }

    @Transactional
    public void importYaml(InputStream yamlStream) {
        YamlImportHistory history = new YamlImportHistory();
        history.setTime(ZonedDateTime.now());

        try {
            YamlParseResult result = parseYaml(yamlStream);

            // Подсчет общего количества объектов для импорта
            int totalObjects = calculateTotalObjects(result);
            history.setImportedObjects(totalObjects);

            // Проверка уникальности в рамках YAML файла
            validateYamlUniqueness(result);

            // Валидация и сохранение каждого объекта
            log.warn("it started 123");
            saveAllColors(result);
            saveAllCountries(result);
            saveAllDifficulties(result);
            saveAllCoordinates(result);
            saveAllLocations(result);
            saveAllPeople(result);
            saveAllDisciplines(result);
            saveAllLabWorks(result);
            log.warn("it finished 123");

            // Успешный импорт
            history.setStatus("SUCCESS");

        } catch (YamlSyntaxException e) {
            // Ошибка синтаксиса YAML
            history.setStatus("SYNTAX_ERROR");
            history.setImportedObjects(0);
            history.setErrorMessage(e.getMessage());
            throw e;

        } catch (ValidationException e) {
            // Ошибка валидации
            history.setStatus("VALIDATION_ERROR");
            history.setImportedObjects(0);
            history.setErrorMessage(e.getMessage());
            throw e;

        } catch (Exception e) {
            // Другие ошибки
            history.setStatus("ERROR");
            history.setImportedObjects(0);
            history.setErrorMessage(e.getMessage());
            throw e;
        } finally {
            // Всегда сохраняем историю, даже при ошибках
            importHistoryRepository.save(history);
        }
    }

    private int calculateTotalObjects(YamlParseResult result) {
        int count = 0;
        count += result.getLabWorks() != null ? result.getLabWorks().size() : 0;
        count += result.getDisciplines() != null ? result.getDisciplines().size() : 0;
        count += result.getPeople() != null ? result.getPeople().size() : 0;
        count += result.getLocations() != null ? result.getLocations().size() : 0;
        count += result.getCoordinates() != null ? result.getCoordinates().size() : 0;
        count += result.getColors() != null ? result.getColors().size() : 0;
        count += result.getCountries() != null ? result.getCountries().size() : 0;
        count += result.getDifficulties() != null ? result.getDifficulties().size() : 0;
        return count;
    }

    private void validateYamlUniqueness(YamlParseResult result) {
        // Проверка уникальности имен дисциплин в YAML
        Set<String> disciplineNames = new HashSet<>();
        for (Discipline discipline : result.getDisciplines()) {
            if (discipline.getName() != null) {
                if (disciplineNames.contains(discipline.getName())) {
                    throw new ValidationException("Discipline name must be unique in YAML: '" + discipline.getName() + "'");
                }
                disciplineNames.add(discipline.getName());
            }
        }

        // Проверка уникальности имен LabWork в YAML
        Set<String> labWorkNames = new HashSet<>();
        for (LabWork labWork : result.getLabWorks()) {
            if (labWork.getName() != null) {
                if (labWorkNames.contains(labWork.getName())) {
                    throw new ValidationException("LabWork name must be unique in YAML: '" + labWork.getName() + "'");
                }
                labWorkNames.add(labWork.getName());
            }
        }

        // Проверка уникальности координат в YAML
        Set<String> coordinates = new HashSet<>();
        for (Coordinate coordinate : result.getCoordinates()) {
            if (coordinate.getX() != null && coordinate.getY() != null) {
                String coordKey = String.format("(%.2f, %.2f)", coordinate.getX(), coordinate.getY());
                if (coordinates.contains(coordKey)) {
                    throw new ValidationException("Coordinate must be unique in YAML: " + coordKey);
                }
                coordinates.add(coordKey);
            }
        }

        // Проверка уникальности passportId в YAML
        Set<String> passportIds = new HashSet<>();
        for (Person person : result.getPeople()) {
            if (person.getPassportId() != null) {
                if (passportIds.contains(person.getPassportId())) {
                    throw new ValidationException("Person passport ID must be unique in YAML: '" + person.getPassportId() + "'");
                }
                passportIds.add(person.getPassportId());
            }
        }
    }

    private void saveAllColors(YamlParseResult result) {
        Map<Color, Color> colorReplacements = new HashMap<>();

        for (Color color : result.getColors()) {
            validationService.validateColor(color);
            Color existing = enumsRepository.findExistingColor(color.getVal());
            if (existing == null) {
                enumsRepository.persistColor(color);
            } else {
                colorReplacements.put(color, existing);
                replaceReference(result.getReferenceMap(), color, existing);
            }
        }

        updateColorReferencesInPeople(result, colorReplacements);
    }

    private void updateColorReferencesInPeople(YamlParseResult result, Map<Color, Color> replacements) {
        if (replacements.isEmpty()) return;

        for (Person person : result.getPeople()) {
            if (person.getHairColor() != null && replacements.containsKey(person.getHairColor())) {
                person.setHairColor(replacements.get(person.getHairColor()));
            }
            if (person.getEyeColor() != null && replacements.containsKey(person.getEyeColor())) {
                person.setEyeColor(replacements.get(person.getEyeColor()));
            }
        }
    }

    private void saveAllCountries(YamlParseResult result) {
        Map<Country, Country> countryReplacements = new HashMap<>();

        for (Country country : result.getCountries()) {
            validationService.validateCountry(country);
            Country existing = enumsRepository.findExistingCountry(country.getVal());
            if (existing == null) {
                enumsRepository.persistCountry(country);
            } else {
                countryReplacements.put(country, existing);
                replaceReference(result.getReferenceMap(), country, existing);
            }
        }

        updateCountryReferencesInPeople(result, countryReplacements);
    }

    private void updateCountryReferencesInPeople(YamlParseResult result, Map<Country, Country> replacements) {
        if (replacements.isEmpty()) return;

        for (Person person : result.getPeople()) {
            if (person.getNationality() != null && replacements.containsKey(person.getNationality())) {
                person.setNationality(replacements.get(person.getNationality()));
            }
        }
    }

    private void saveAllDifficulties(YamlParseResult result) {
        Map<Difficulty, Difficulty> difficultyReplacements = new HashMap<>();

        for (Difficulty difficulty : result.getDifficulties()) {
            validationService.validateDifficulty(difficulty);
            Difficulty existing = enumsRepository.findExistingDifficulty(difficulty.getVal());
            if (existing == null) {
                enumsRepository.persistDifficulty(difficulty);
            } else {
                difficultyReplacements.put(difficulty, existing);
                replaceReference(result.getReferenceMap(), difficulty, existing);
            }
        }

        updateDifficultyReferencesInLabWorks(result, difficultyReplacements);
    }

    private void updateDifficultyReferencesInLabWorks(YamlParseResult result, Map<Difficulty, Difficulty> replacements) {
        if (replacements.isEmpty()) return;

        for (LabWork labWork : result.getLabWorks()) {
            if (labWork.getDifficulty() != null && replacements.containsKey(labWork.getDifficulty())) {
                labWork.setDifficulty(replacements.get(labWork.getDifficulty()));
            }
        }
    }

    private void saveAllCoordinates(YamlParseResult result) {
        for (Coordinate coordinate : result.getCoordinates()) {
            validationService.validateCoordinate(coordinate);
            coordinateRepository.save(coordinate);
        }
    }

    private void saveAllLocations(YamlParseResult result) {
        for (Location location : result.getLocations()) {
            validationService.validateLocation(location);
            locationRepository.save(location);
        }
    }

    private void saveAllPeople(YamlParseResult result) {
        for (Person person : result.getPeople()) {
            validationService.validatePerson(person);
            personRepository.save(person);
        }
    }

    private void saveAllDisciplines(YamlParseResult result) {
        for (Discipline discipline : result.getDisciplines()) {
            validationService.validateDiscipline(discipline);
            disciplineRepository.save(discipline);
        }
    }

    private void saveAllLabWorks(YamlParseResult result) {
        for (LabWork labWork : result.getLabWorks()) {
            // Установка даты создания, если не указана
            if (labWork.getCreationDate() == null) {
                labWork.setCreationDate(ZonedDateTime.now());
            }

            // Валидация LabWork и его зависимостей
            validationService.validateLabWork(labWork);

            labWorkRepository.save(labWork);
        }
    }

    private void replaceReference(Map<String, Object> referenceMap, Object oldEntity, Object newEntity) {
        if (referenceMap == null) return;

        referenceMap.entrySet().stream().filter(entry -> entry.getValue() == oldEntity).forEach(entry -> entry.setValue(newEntity));
    }

    // Метод для получения истории импорта
    public List<YamlImportHistory> getImportHistory(int limit) {
        return importHistoryRepository.findRecentImports(limit);
    }
}