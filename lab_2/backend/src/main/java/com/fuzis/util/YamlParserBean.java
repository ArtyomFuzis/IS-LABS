package com.fuzis.util;

import com.fuzis.entity.*;
import com.fuzis.exception.YamlSyntaxException;
import com.fuzis.service.YamlReferenceService;
import com.fuzis.transferdata.inner.YamlParseResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.InputStream;
import java.util.*;

@ApplicationScoped
public class YamlParserBean {

    @Inject
    private YamlReferenceService yamlReferenceService;

    public YamlParseResult parseYaml(InputStream yamlStream) {
        try {
            LoaderOptions loaderOptions = new LoaderOptions();
            Yaml yaml = new Yaml(new Constructor(List.class, loaderOptions));
            List<Map<String, Object>> yamlData = yaml.load(yamlStream);

            if (yamlData == null) {
                throw new YamlSyntaxException("YAML file is empty or invalid");
            }

            // Создаем результат парсинга
            YamlParseResult result = new YamlParseResult();
            result.setReferenceMap(new HashMap<>());
            result.setColors(new ArrayList<>());
            result.setCountries(new ArrayList<>());
            result.setDifficulties(new ArrayList<>());
            result.setCoordinates(new ArrayList<>());
            result.setLocations(new ArrayList<>());
            result.setPeople(new ArrayList<>());
            result.setDisciplines(new ArrayList<>());
            result.setLabWorks(new ArrayList<>());

            // Сначала создаем все базовые объекты
            for (Map<String, Object> item : yamlData) {
                String type = (String) item.get("type");
                if (type != null) {
                    createObjectFromItem(item, type, result);
                }
            }

            // Затем разрешаем ссылки между объектами
            yamlReferenceService.resolveReferences(yamlData, result);

            return result;

        } catch (YAMLException e) {
            throw new YamlSyntaxException("Invalid YAML syntax: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new YamlSyntaxException("Error parsing YAML: " + e.getMessage(), e);
        }
    }

    private void createObjectFromItem(Map<String, Object> item, String type, YamlParseResult result) {
        switch (type) {
            case "Color":
                createColor(item, result);
                break;
            case "Country":
                createCountry(item, result);
                break;
            case "Difficulty":
                createDifficulty(item, result);
                break;
            case "Coordinate":
                createCoordinate(item, result);
                break;
            case "Location":
                createLocation(item, result);
                break;
            case "Person":
                createPerson(item, result);
                break;
            case "Discipline":
                createDiscipline(item, result);
                break;
            case "LabWork":
                createLabWork(item, result);
                break;
            default:
                throw new YamlSyntaxException("Unknown type in YAML: " + type);
        }
    }

    private void createColor(Map<String, Object> item, YamlParseResult result) {
        Color color = new Color();

        // Если color задан как строка (например, "BLUE")
        if (item.get("val") != null) {
            color.setVal(item.get("val").toString());
        } else if (item.get("value") != null) {
            color.setVal(item.get("value").toString());
        } else if (item.get("name") != null) {
            color.setVal(item.get("name").toString());
        }

        if (color.getVal() != null) {
            result.getColors().add(color);

            // Если есть identifier, добавляем в referenceMap
            String identifier = (String) item.get("identifier");
            if (identifier != null && identifier.startsWith("@")) {
                result.getReferenceMap().put(identifier, color);
            }
        }
    }

    private void createCountry(Map<String, Object> item, YamlParseResult result) {
        Country country = new Country();

        if (item.get("val") != null) {
            country.setVal(item.get("val").toString());
        } else if (item.get("value") != null) {
            country.setVal(item.get("value").toString());
        } else if (item.get("name") != null) {
            country.setVal(item.get("name").toString());
        }

        if (country.getVal() != null) {
            result.getCountries().add(country);

            String identifier = (String) item.get("identifier");
            if (identifier != null && identifier.startsWith("@")) {
                result.getReferenceMap().put(identifier, country);
            }
        }
    }

    private void createDifficulty(Map<String, Object> item, YamlParseResult result) {
        Difficulty difficulty = new Difficulty();

        if (item.get("val") != null) {
            difficulty.setVal(item.get("val").toString());
        } else if (item.get("value") != null) {
            difficulty.setVal(item.get("value").toString());
        } else if (item.get("name") != null) {
            difficulty.setVal(item.get("name").toString());
        }

        if (difficulty.getVal() != null) {
            result.getDifficulties().add(difficulty);

            String identifier = (String) item.get("identifier");
            if (identifier != null && identifier.startsWith("@")) {
                result.getReferenceMap().put(identifier, difficulty);
            }
        }
    }

    private void createCoordinate(Map<String, Object> item, YamlParseResult result) {
        Coordinate coordinate = new Coordinate();

        if (item.get("x") != null) {
            coordinate.setX(convertToDouble(item.get("x")));
        }

        if (item.get("y") != null) {
            coordinate.setY(convertToDouble(item.get("y")));
        }

        if (coordinate.getX() != null && coordinate.getY() != null) {
            result.getCoordinates().add(coordinate);

            String identifier = (String) item.get("identifier");
            if (identifier != null && identifier.startsWith("@")) {
                result.getReferenceMap().put(identifier, coordinate);
            }
        }
    }

    private void createLocation(Map<String, Object> item, YamlParseResult result) {
        Location location = new Location();

        location.setName((String) item.get("name"));

        if (item.get("x") != null) {
            location.setX(convertToDouble(item.get("x")));
        }

        if (item.get("y") != null) {
            location.setY(convertToDouble(item.get("y")));
        }

        if (item.get("z") != null) {
            location.setZ(convertToDouble(item.get("z")));
        }

        result.getLocations().add(location);

        String identifier = (String) item.get("identifier");
        if (identifier != null && identifier.startsWith("@")) {
            result.getReferenceMap().put(identifier, location);
        }
    }

    private void createPerson(Map<String, Object> item, YamlParseResult result) {
        Person person = new Person();

        person.setName((String) item.get("name"));
        person.setPassportId((String) item.get("passportId"));

        // Обработка полей, которые могут быть ссылками или значениями
        // Они будут обработаны в YamlReferenceService

        result.getPeople().add(person);

        String identifier = (String) item.get("identifier");
        if (identifier != null && identifier.startsWith("@")) {
            result.getReferenceMap().put(identifier, person);
        }
    }

    private void createDiscipline(Map<String, Object> item, YamlParseResult result) {
        Discipline discipline = new Discipline();

        discipline.setName((String) item.get("name"));

        if (item.get("labsCount") != null) {
            discipline.setLabsCount(convertToInteger(item.get("labsCount")));
        }


        result.getDisciplines().add(discipline);

        String identifier = (String) item.get("identifier");
        if (identifier != null && identifier.startsWith("@")) {
            result.getReferenceMap().put(identifier, discipline);
        }
    }

    private void createLabWork(Map<String, Object> item, YamlParseResult result) {
        LabWork labWork = new LabWork();

        labWork.setName((String) item.get("name"));

        if (item.get("description") != null) {
            labWork.setDescription((String) item.get("description"));
        }

        if (item.get("minimalPoint") != null) {
            labWork.setMinimalPoint(convertToDouble(item.get("minimalPoint")));
        }

        if (item.get("maximumPoint") != null) {
            labWork.setMaximalPoint(convertToDouble(item.get("maximumPoint")));
        }

        // Обработка полей, которые могут быть ссылками
        // Они будут обработаны в YamlReferenceService

        result.getLabWorks().add(labWork);

        String identifier = (String) item.get("identifier");
        if (identifier != null && identifier.startsWith("@")) {
            result.getReferenceMap().put(identifier, labWork);
        }
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
}