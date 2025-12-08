package com.fuzis.util;

import com.fuzis.entity.*;
import com.fuzis.transferdata.inner.YamlParseResult;
import com.fuzis.service.YamlReferenceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class YamlParserBean {

    @Inject
    private YamlReferenceService yamlReferenceService;

    public YamlParseResult parseYaml(InputStream yamlStream) {
        LoaderOptions loaderOptions = new LoaderOptions();
        Yaml yaml = new Yaml(new Constructor(List.class, loaderOptions));
        List<Map<String, Object>> yamlData = yaml.load(yamlStream);

        YamlParseResult result = new YamlParseResult();

        // Первый проход: создаем все объекты без разрешения ссылок
        for (Map<String, Object> item : yamlData) {
            String type = (String) item.get("type");
            String identifier = (String) item.get("identifier");

            Object entity = createEntity(type, item, result);

            if (identifier != null) {
                result.getReferenceMap().put(identifier, entity);
            }
        }

        // Второй проход: разрешаем ссылки
        yamlReferenceService.resolveReferences(yamlData, result);

        return result;
    }

    private Object createEntity(String type, Map<String, Object> data, YamlParseResult result) {
        return switch (type) {
            case "Coordinate" -> createCoordinate(data, result);
            case "Location" -> createLocation(data, result);
            case "Person" -> createPerson(data, result);
            case "Color" -> createColor(data, result);
            case "Country" -> createCountry(data, result);
            case "Difficulty" -> createDifficulty(data, result);
            case "Discipline" -> createDiscipline(data, result);
            case "LabWork" -> createLabWork(data, result);
            default -> throw new IllegalArgumentException("Unknown entity type: " + type);
        };
    }

    private Coordinate createCoordinate(Map<String, Object> data, YamlParseResult result) {
        Coordinate coordinate = new Coordinate();
        coordinate.setX(convertToDouble(data.get("x")));
        coordinate.setY(convertToDouble(data.get("y")));

        result.getCoordinates().add(coordinate);
        return coordinate;
    }

    private Location createLocation(Map<String, Object> data, YamlParseResult result) {
        Location location = new Location();
        location.setName((String) data.get("name"));
        location.setX(convertToDouble(data.get("x")));
        location.setY(convertToDouble(data.get("y")));

        if (data.containsKey("z")) {
            location.setZ(convertToDouble(data.get("z")));
        }

        result.getLocations().add(location);
        return location;
    }

    private Person createPerson(Map<String, Object> data, YamlParseResult result) {
        Person person = new Person();
        person.setName((String) data.get("name"));
        person.setPassportId((String) data.get("passportId"));

        // Поля-ссылки будут установлены позже в YamlReferenceService
        result.getPeople().add(person);
        return person;
    }

    private Color createColor(Map<String, Object> data, YamlParseResult result) {
        Color color = new Color();
        color.setVal(data.get("val") != null ?
                data.get("val").toString() :
                resolveEnumValue(data, "Color"));

        result.getColors().add(color);
        return color;
    }

    private Country createCountry(Map<String, Object> data, YamlParseResult result) {
        Country country = new Country();
        country.setVal(data.get("val") != null ?
                data.get("val").toString() :
                resolveEnumValue(data, "Country"));

        result.getCountries().add(country);
        return country;
    }

    private Difficulty createDifficulty(Map<String, Object> data, YamlParseResult result) {
        Difficulty difficulty = new Difficulty();
        difficulty.setVal(data.get("val") != null ?
                data.get("val").toString() :
                resolveEnumValue(data, "Difficulty"));

        result.getDifficulties().add(difficulty);
        return difficulty;
    }

    private Discipline createDiscipline(Map<String, Object> data, YamlParseResult result) {
        Discipline discipline = new Discipline();
        discipline.setName((String) data.get("name"));

        if (data.containsKey("labsCount")) {
            discipline.setLabsCount(convertToInteger(data.get("labsCount")));
        }

        result.getDisciplines().add(discipline);
        return discipline;
    }

    private LabWork createLabWork(Map<String, Object> data, YamlParseResult result) {
        LabWork labWork = new LabWork();
        labWork.setName((String) data.get("name"));
        labWork.setDescription((String) data.get("description"));

        if (data.containsKey("minimalPoint")) {
            labWork.setMinimalPoint(convertToDouble(data.get("minimalPoint")));
        }

        if (data.containsKey("maximalPoint")) {
            labWork.setMaximalPoint(convertToDouble(data.get("maximalPoint")));
        }

        result.getLabWorks().add(labWork);
        return labWork;
    }

    private Double convertToDouble(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).doubleValue();
        return Double.parseDouble(value.toString());
    }

    private Integer convertToInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }

    private String resolveEnumValue(Map<String, Object> data, String type) {
        Object value = data.get("id") != null ? data.get("id") : data.get("val");
        if (value instanceof Integer) {
            // Если задан ID, делегируем поиск в базе данных YamlReferenceRepository
            // через YamlReferenceService
            return yamlReferenceService.resolveEnumValueById(type, (Integer) value);
        }
        return value != null ? value.toString() : null;
    }
}