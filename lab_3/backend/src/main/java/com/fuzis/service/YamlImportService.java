package com.fuzis.service;

import com.fuzis.annotation.CacheStatisticsLogging;
import com.fuzis.database.*;
import com.fuzis.entity.*;
import com.fuzis.exception.ValidationException;
import com.fuzis.exception.YamlSyntaxException;
import com.fuzis.transferdata.inner.YamlParseResult;
import com.fuzis.util.YamlParserBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Status;
import jakarta.transaction.Synchronization;
import jakarta.transaction.TransactionSynchronizationRegistry;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.ConnectException;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@RequestScoped
@CacheStatisticsLogging
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

    @Inject
    private MinioService minioService;

    @Inject
    private YamlImportHistoryRepository historyRepository;

    public YamlParseResult parseYaml(InputStream yamlStream) {
        return yamlParser.parseYaml(yamlStream);
    }

    @Transactional
    public void importYaml(InputStream yamlStream, long size) throws Exception {

        YamlImportHistory history = new YamlImportHistory();
        history.setTime(ZonedDateTime.now());

        String generatedName = "import-" + UUID.randomUUID() + ".yml";
        history.setOriginalFilename(generatedName);

        String minioFilename = null;

        try {
            byte[] data = yamlStream.readAllBytes();

            minioFilename = minioService.uploadFile(
                    new ByteArrayInputStream(data),
                    generatedName,
                    size
            );
            history.setFilename(minioFilename);

            YamlParseResult result = parseYaml(
                    new ByteArrayInputStream(data)
            );

            int totalObjects = calculateTotalObjects(result);
            history.setImportedObjects(totalObjects);

            validateYamlUniqueness(result);

            saveAllColors(result);
            saveAllCountries(result);
            saveAllDifficulties(result);
            saveAllCoordinates(result);
            saveAllLocations(result);
            saveAllPeople(result);
            saveAllDisciplines(result);
            saveAllLabWorks(result);

            registerTransactionCallbacks(history, minioFilename);

        } catch (YamlSyntaxException e) {
            rollbackMinio(minioFilename);
            history.setStatus("SYNTAX_ERROR");
            history.setImportedObjects(0);
            history.setErrorMessage(e.getMessage());
            importHistoryRepository.save(history);
            throw e;

        } catch (ValidationException e) {
            rollbackMinio(minioFilename);
            history.setStatus("VALIDATION_ERROR");
            history.setImportedObjects(0);
            history.setErrorMessage(e.getMessage());
            importHistoryRepository.save(history);
            throw e;

        } catch (ConnectException e) {
            history.setStatus("MINIO_ERROR");
            history.setImportedObjects(0);
            history.setErrorMessage(e.getMessage());
            importHistoryRepository.save(history);
            throw e;
        }
        catch (Exception e) {
            history.setStatus("ERROR");
            history.setImportedObjects(0);
            history.setErrorMessage(e.getMessage());
            importHistoryRepository.save(history);
            rollbackMinio(minioFilename);
            throw new RuntimeException(e);
        }
    }

    @Inject
    TransactionSynchronizationRegistry tsr;

    private void registerTransactionCallbacks(YamlImportHistory history, String minioFilename) {

        tsr.registerInterposedSynchronization(new Synchronization() {

            @Override
            public void afterCompletion(int status) {
                if (status == Status.STATUS_COMMITTED) {
                    history.setStatus("SUCCESS");
                    history.setImportedObjects(0);
                } else {
                    history.setStatus("TRANSACTION_ERROR");
                    rollbackMinio(minioFilename);
                }
                importHistoryRepository.save(history);
            }

            @Override
            public void beforeCompletion() {
            }
        });
    }

    private void rollbackMinio(String filename) {
        if (filename == null) return;

        try {
            minioService.deleteFile(filename);
            log.warn("MinIO rollback: file {} deleted", filename);
        } catch (Exception ex) {
            log.error("Failed to rollback MinIO file {}", filename, ex);
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

        Set<String> disciplineNames = new HashSet<>();
        for (Discipline discipline : result.getDisciplines()) {
            if (discipline.getName() != null) {
                if (disciplineNames.contains(discipline.getName())) {
                    throw new ValidationException("Discipline name must be unique in YAML: '" + discipline.getName() + "'");
                }
                disciplineNames.add(discipline.getName());
            }
        }


        Set<String> labWorkNames = new HashSet<>();
        for (LabWork labWork : result.getLabWorks()) {
            if (labWork.getName() != null) {
                if (labWorkNames.contains(labWork.getName())) {
                    throw new ValidationException("LabWork name must be unique in YAML: '" + labWork.getName() + "'");
                }
                labWorkNames.add(labWork.getName());
            }
        }


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

            if (labWork.getCreationDate() == null) {
                labWork.setCreationDate(ZonedDateTime.now());
            }


            validationService.validateLabWork(labWork);

            labWorkRepository.save(labWork);
        }
    }

    private void replaceReference(Map<String, Object> referenceMap, Object oldEntity, Object newEntity) {
        if (referenceMap == null) return;

        referenceMap.entrySet().stream().filter(entry -> entry.getValue() == oldEntity).forEach(entry -> entry.setValue(newEntity));
    }


    public List<YamlImportHistory> getImportHistory(int limit) {
        return importHistoryRepository.findRecentImports(limit);
    }

    public FileDownloadResult downloadImportedFile(Integer historyId) {
        YamlImportHistory history = importHistoryRepository.get(historyId);

        if (history == null) {
            throw new NotFoundException("Import history, id: " + historyId + " not found");
        }

        String filename = history.getFilename();
        if (filename == null || filename.isBlank()) {
            throw new NotFoundException("No file created");
        }

        try {
            InputStream fileStream = minioService.downloadFile(filename);

            String downloadFilename = history.getOriginalFilename();
            return new FileDownloadResult(fileStream, downloadFilename);

        } catch (Exception e) {
            throw new RuntimeException("Unable to load file: " + e.getMessage(), e);
        }
    }

    @Getter
    public static class FileDownloadResult {
        private final InputStream inputStream;
        private final String filename;

        public FileDownloadResult(InputStream inputStream, String filename) {
            this.inputStream = inputStream;
            this.filename = filename;
        }

    }
}