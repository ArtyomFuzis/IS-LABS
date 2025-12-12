package com.fuzis.service;

import com.fuzis.database.*;
import com.fuzis.entity.*;
import com.fuzis.transferdata.inner.YamlParseResult;
import com.fuzis.util.Locks;
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

    @Inject
    Locks locks;

    public YamlParseResult parseYaml(InputStream yamlStream) {
        return yamlParser.parseYaml(yamlStream);
    }

    @Transactional(value = TxType.REQUIRED, rollbackOn = Exception.class)
    public void importYaml(InputStream yamlStream) {
        YamlParseResult result = parseYaml(yamlStream);
        synchronized (locks.getLock_insert_update()) {

            validationService.validateYamlResult(result);

            saveAllColors(result);
            saveAllCountries(result);
            saveAllDifficulties(result);
            saveAllCoordinates(result);
            saveAllLocations(result);
            saveAllPeople(result);
            saveAllDisciplines(result);
            saveAllLabWorks(result);

            flushAllRepositories();
        }
    }

    private void saveAllColors(YamlParseResult result) {
        Map<Color, Color> colorReplacements = new HashMap<>();

        for (Color color : result.getColors()) {
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
            coordinateRepository.save(coordinate);
        }
    }

    private void saveAllLocations(YamlParseResult result) {
        for (Location location : result.getLocations()) {
            locationRepository.save(location);
        }
    }

    private void saveAllPeople(YamlParseResult result) {
        for (Person person : result.getPeople()) {
            personRepository.save(person);
        }
    }

    private void saveAllDisciplines(YamlParseResult result) {
        for (Discipline discipline : result.getDisciplines()) {
            disciplineRepository.save(discipline);
        }
    }

    private void saveAllLabWorks(YamlParseResult result) {
        for (LabWork labWork : result.getLabWorks()) {
            
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
        enumsRepository.flush();
        coordinateRepository.flush();
        locationRepository.flush();
        personRepository.flush();
        disciplineRepository.flush();
        labWorkRepository.flush();
    }
}