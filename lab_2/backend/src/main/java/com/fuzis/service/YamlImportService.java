package com.fuzis.service;

import com.fuzis.database.*;
import com.fuzis.entity.*;
import com.fuzis.service.ValidationService;
import com.fuzis.transferdata.inner.YamlParseResult;
import com.fuzis.util.YamlParserBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

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

    public YamlParseResult parseYaml(InputStream yamlStream) {
        return yamlParser.parseYaml(yamlStream);
    }

    @Transactional(value = TxType.REQUIRED, rollbackOn = Exception.class)
    public void importYaml(InputStream yamlStream) {
        YamlParseResult result = parseYaml(yamlStream);

        try {
            // Комплексная валидация всего результата
            if (!validationService.validateYamlResult(result)) {
                throw new IllegalArgumentException("YAML validation failed");
            }

            // Сохраняем в правильном порядке с учетом зависимостей
            // Вся операция выполняется в одной транзакции
            saveAllColors(result);
            saveAllCountries(result);
            saveAllDifficulties(result);
            saveAllCoordinates(result);
            saveAllLocations(result);
            saveAllPeople(result);
            saveAllDisciplines(result);
            saveAllLabWorks(result);

            // Принудительно флашим изменения для проверки constraint violations
            flushAllRepositories();

        } catch (Exception e) {
            // Транзакция автоматически откатится благодаря rollbackOn = Exception.class
            throw new RuntimeException("Failed to import YAML: " + e.getMessage(), e);
        }
    }

    private void saveAllColors(YamlParseResult result) {
        Map<Color, Color> colorReplacements = new HashMap<>();

        for (Color color : result.getColors()) {
            // Валидация через ValidationService
            if (!validationService.validateColor(color)) {
                throw new IllegalArgumentException("Color validation failed");
            }

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
            // Валидация через ValidationService
            if (!validationService.validateCountry(country)) {
                throw new IllegalArgumentException("Country validation failed");
            }

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
            // Валидация через ValidationService
            if (!validationService.validateDifficulty(difficulty)) {
                throw new IllegalArgumentException("Difficulty validation failed");
            }

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
            // Валидация через ValidationService
            if (!validationService.validateCoordinate(coordinate)) {
                throw new IllegalArgumentException("Coordinate validation failed");
            }

            coordinateRepository.save(coordinate);
        }
    }

    private void saveAllLocations(YamlParseResult result) {
        for (Location location : result.getLocations()) {
            // Валидация через ValidationService
            if (!validationService.validateLocation(location)) {
                throw new IllegalArgumentException("Location validation failed");
            }

            locationRepository.save(location);
        }
    }

    private void saveAllPeople(YamlParseResult result) {
        for (Person person : result.getPeople()) {
            // Валидация через ValidationService
            if (!validationService.validatePerson(person)) {
                throw new IllegalArgumentException("Person validation failed");
            }

            // Валидация зависимостей через ValidationService
            if (!validationService.validatePersonDependencies(person)) {
                throw new IllegalArgumentException("Person dependencies validation failed");
            }

            personRepository.save(person);
        }
    }

    private void saveAllDisciplines(YamlParseResult result) {
        for (Discipline discipline : result.getDisciplines()) {
            // Валидация через ValidationService
            if (!validationService.validateDiscipline(discipline)) {
                throw new IllegalArgumentException("Discipline validation failed");
            }

            disciplineRepository.save(discipline);
        }
    }

    private void saveAllLabWorks(YamlParseResult result) {
        for (LabWork labWork : result.getLabWorks()) {
            // Валидация через ValidationService
            if (!validationService.validateLabWork(labWork)) {
                throw new IllegalArgumentException("LabWork validation failed");
            }

            // Валидация зависимостей через ValidationService
            if (!validationService.validateLabWorkDependencies(labWork)) {
                throw new IllegalArgumentException("LabWork dependencies validation failed");
            }

            // Устанавливаем дату создания, если не задана
            if (labWork.getCreationDate() == null) {
                labWork.setCreationDate(ZonedDateTime.now());
            }

            labWorkRepository.save(labWork);
        }
    }

    private void replaceReference(Map<String, Object> referenceMap,
                                  Object oldEntity, Object newEntity) {
        if (referenceMap == null) return;

        referenceMap.entrySet().stream()
                .filter(entry -> entry.getValue() == oldEntity)
                .forEach(entry -> entry.setValue(newEntity));
    }

    private void flushAllRepositories() {
        // Принудительно флашим EntityManager для проверки constraint violations
        enumsRepository.flush();
        coordinateRepository.flush();
        locationRepository.flush();
        personRepository.flush();
        disciplineRepository.flush();
        labWorkRepository.flush();
    }
}