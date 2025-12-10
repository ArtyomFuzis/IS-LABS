package com.fuzis.service;

import com.fuzis.database.YamlReferenceRepository;
import com.fuzis.entity.*;
import com.fuzis.transferdata.inner.YamlParseResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class YamlReferenceService {

    @Inject
    private YamlReferenceRepository referenceRepository;

    public void resolveReferences(List<Map<String, Object>> yamlData, YamlParseResult result) {
        for (Map<String, Object> item : yamlData) {
            String type = (String) item.get("type");

            if ("Person".equals(type)) {
                resolvePersonReferences(item, result);
            } else if ("LabWork".equals(type)) {
                resolveLabWorkReferences(item, result);
            }
        }
    }

    private void resolvePersonReferences(Map<String, Object> data, YamlParseResult result) {
        String name = (String) data.get("name");
        Person person = result.getPeople().stream()
                .filter(p -> name.equals(p.getName()))
                .findFirst()
                .orElse(null);

        if (person == null) return;

        if (data.containsKey("hairColor")) {
            Object hairColorRef = data.get("hairColor");
            Color hairColor = resolveColorReference(hairColorRef, result);
            person.setHairColor(hairColor);
        }

        if (data.containsKey("eyeColor")) {
            Object eyeColorRef = data.get("eyeColor");
            Color eyeColor = resolveColorReference(eyeColorRef, result);
            person.setEyeColor(eyeColor);
        }

        if (data.containsKey("location")) {
            Object locationRef = data.get("location");
            Location location = resolveLocationReference(locationRef, result);
            person.setLocation(location);
        }
        
        if (data.containsKey("nationality")) {
            Object nationalityRef = data.get("nationality");
            Country nationality = resolveCountryReference(nationalityRef, result);
            person.setNationality(nationality);
        }
    }

    private Color resolveColorReference(Object ref, YamlParseResult result) {
        if (ref instanceof String) {
            String refStr = (String) ref;
            if (refStr.startsWith("@")) {
                
                Object entity = result.getReferenceMap().get(refStr);
                if (entity instanceof Color) {
                    return (Color) entity;
                }
            } else {
                
                Color color = new Color();
                color.setVal(refStr);

                Color existingInResult = result.getColors().stream()
                        .filter(c -> refStr.equals(c.getVal()))
                        .findFirst()
                        .orElse(null);

                if (existingInResult != null) {
                    return existingInResult;
                }

                result.getColors().add(color);
                return color;
            }
        } else if (ref instanceof Integer) {
            
            Integer id = (Integer) ref;
            Color color = referenceRepository.findColorById(id);
            if (color == null) {
                throw new IllegalArgumentException("Color with id " + id + " not found");
            }
            return color;
        } else if (ref instanceof Map) {
            
            Map<String, Object> colorData = (Map<String, Object>) ref;
            Color color = new Color();
            color.setVal(colorData.get("val").toString());

            String identifier = (String) colorData.get("identifier");
            if (identifier != null && identifier.startsWith("@")) {
                result.getReferenceMap().put(identifier, color);
            }

            result.getColors().add(color);
            return color;
        }
        return null;
    }

    private Location resolveLocationReference(Object ref, YamlParseResult result) {
        if (ref instanceof String) {
            String refStr = (String) ref;
            if (refStr.startsWith("@")) {
                
                Object entity = result.getReferenceMap().get(refStr);
                if (entity instanceof Location) {
                    return (Location) entity;
                }
            }
        } else if (ref instanceof Integer) {
            
            Integer id = (Integer) ref;
            Location location = referenceRepository.findLocationById(id);
            if (location == null) {
                throw new IllegalArgumentException("Location with id " + id + " not found");
            }
            return location;
        } else if (ref instanceof Map) {
            
            Map<String, Object> locationData = (Map<String, Object>) ref;
            String identifier = (String) locationData.get("identifier");

            if (identifier != null && identifier.startsWith("@")) {
                
                Object existingEntity = result.getReferenceMap().get(identifier);
                if (existingEntity instanceof Location) {
                    return (Location) existingEntity;
                }
            }

            
            Location location = new Location();
            location.setName((String) locationData.get("name"));
            location.setX(convertToDouble(locationData.get("x")));
            location.setY(convertToDouble(locationData.get("y")));

            if (locationData.containsKey("z")) {
                location.setZ(convertToDouble(locationData.get("z")));
            }

            result.getLocations().add(location);

            if (identifier != null && identifier.startsWith("@")) {
                result.getReferenceMap().put(identifier, location);
            }

            return location;
        }
        return null;
    }

    private Country resolveCountryReference(Object ref, YamlParseResult result) {
        if (ref instanceof String) {
            String refStr = (String) ref;
            if (refStr.startsWith("@")) {
                Object entity = result.getReferenceMap().get(refStr);
                if (entity instanceof Country) {
                    return (Country) entity;
                }
            } else {
                
                Country country = new Country();
                country.setVal(refStr);
                return country;
            }
        } else if (ref instanceof Integer) {
            
            Integer id = (Integer) ref;
            Country country = referenceRepository.findCountryById(id);
            if (country == null) {
                throw new IllegalArgumentException("Country with id " + id + " not found");
            }
            return country;
        } else if (ref instanceof Map) {
            
            Map<String, Object> countryData = (Map<String, Object>) ref;
            Country country = new Country();
            country.setVal(countryData.get("val").toString());

            String identifier = (String) countryData.get("identifier");
            if (identifier != null && identifier.startsWith("@")) {
                result.getReferenceMap().put(identifier, country);
            }

            return country;
        }
        return null;
    }

    private void resolveLabWorkReferences(Map<String, Object> data, YamlParseResult result) {
        
        String name = (String) data.get("name");
        LabWork labWork = result.getLabWorks().stream()
                .filter(l -> name.equals(l.getName()))
                .findFirst()
                .orElse(null);

        if (labWork == null) return;

        
        if (data.containsKey("coordinate")) {
            Object coordinateRef = data.get("coordinate");
            Coordinate coordinate = resolveCoordinateReference(coordinateRef, result);
            labWork.setCoordinate(coordinate);
        }

        
        if (data.containsKey("difficulty")) {
            Object difficultyRef = data.get("difficulty");
            Difficulty difficulty = resolveDifficultyReference(difficultyRef, result);
            labWork.setDifficulty(difficulty);
        }

        
        if (data.containsKey("discipline")) {
            Object disciplineRef = data.get("discipline");
            Discipline discipline = resolveDisciplineReference(disciplineRef, result);
            labWork.setDiscipline(discipline);
        }

        
        if (data.containsKey("author")) {
            Object authorRef = data.get("author");
            Person author = resolvePersonReference(authorRef, result);
            labWork.setAuthor(author);
        }
    }

    private Coordinate resolveCoordinateReference(Object ref, YamlParseResult result) {
        if (ref instanceof String) {
            String refStr = (String) ref;
            if (refStr.startsWith("@")) {
                Object entity = result.getReferenceMap().get(refStr);
                if (entity instanceof Coordinate) {
                    return (Coordinate) entity;
                }
            }
        } else if (ref instanceof Integer) {
            
            Integer id = (Integer) ref;
            Coordinate coordinate = referenceRepository.findCoordinateById(id);
            if (coordinate == null) {
                throw new IllegalArgumentException("Coordinate with id " + id + " not found");
            }
            return coordinate;
        } else if (ref instanceof Map) {
            
            Map<String, Object> coordData = (Map<String, Object>) ref;
            String identifier = (String) coordData.get("identifier");

            if (identifier != null && identifier.startsWith("@")) {
                Object existingEntity = result.getReferenceMap().get(identifier);
                if (existingEntity instanceof Coordinate) {
                    return (Coordinate) existingEntity;
                }
            }

            Coordinate coordinate = new Coordinate();
            coordinate.setX(convertToDouble(coordData.get("x")));
            coordinate.setY(convertToDouble(coordData.get("y")));

            result.getCoordinates().add(coordinate);

            if (identifier != null && identifier.startsWith("@")) {
                result.getReferenceMap().put(identifier, coordinate);
            }

            return coordinate;
        }
        return null;
    }

    private Difficulty resolveDifficultyReference(Object ref, YamlParseResult result) {
        if (ref instanceof String) {
            String refStr = (String) ref;
            if (refStr.startsWith("@")) {
                Object entity = result.getReferenceMap().get(refStr);
                if (entity instanceof Difficulty) {
                    return (Difficulty) entity;
                }
            } else {
                Difficulty difficulty = new Difficulty();
                difficulty.setVal(refStr);
                return difficulty;
            }
        } else if (ref instanceof Integer) {
            Integer id = (Integer) ref;
            Difficulty difficulty = referenceRepository.findDifficultyById(id);
            if (difficulty == null) {
                throw new IllegalArgumentException("Difficulty with id " + id + " not found");
            }
            return difficulty;
        }
        return null;
    }

    private Discipline resolveDisciplineReference(Object ref, YamlParseResult result) {
        if (ref instanceof String) {
            String refStr = (String) ref;
            if (refStr.startsWith("@")) {
                Object entity = result.getReferenceMap().get(refStr);
                if (entity instanceof Discipline) {
                    return (Discipline) entity;
                }
            }
        } else if (ref instanceof Integer) {
            Integer id = (Integer) ref;
            Discipline discipline = referenceRepository.findDisciplineById(id);
            if (discipline == null) {
                throw new IllegalArgumentException("Discipline with id " + id + " not found");
            }
            return discipline;
        } else if (ref instanceof Map) {
            Map<String, Object> discData = (Map<String, Object>) ref;
            String identifier = (String) discData.get("identifier");

            if (identifier != null && identifier.startsWith("@")) {
                Object existingEntity = result.getReferenceMap().get(identifier);
                if (existingEntity instanceof Discipline) {
                    return (Discipline) existingEntity;
                }
            }

            Discipline discipline = new Discipline();
            discipline.setName((String) discData.get("name"));

            if (discData.containsKey("labsCount")) {
                discipline.setLabsCount(convertToInteger(discData.get("labsCount")));
            }

            result.getDisciplines().add(discipline);

            if (identifier != null && identifier.startsWith("@")) {
                result.getReferenceMap().put(identifier, discipline);
            }

            return discipline;
        }
        return null;
    }

    private Person resolvePersonReference(Object ref, YamlParseResult result) {
        if (ref instanceof String) {
            String refStr = (String) ref;
            if (refStr.startsWith("@")) {
                Object entity = result.getReferenceMap().get(refStr);
                if (entity instanceof Person) {
                    return (Person) entity;
                }
            }
        } else if (ref instanceof Integer) {
            Integer id = (Integer) ref;
            Person person = referenceRepository.findPersonById(id);
            if (person == null) {
                throw new IllegalArgumentException("Person with id " + id + " not found");
            }
            return person;
        } else if (ref instanceof Map) {
            Map<String, Object> personData = (Map<String, Object>) ref;
            String identifier = (String) personData.get("identifier");

            if (identifier != null && identifier.startsWith("@")) {
                Object existingEntity = result.getReferenceMap().get(identifier);
                if (existingEntity instanceof Person) {
                    return (Person) existingEntity;
                }
            }

            Person person = createPersonFromMap(personData, result);

            if (identifier != null && identifier.startsWith("@")) {
                result.getReferenceMap().put(identifier, person);
            }

            return person;
        }
        return null;
    }

    private Person createPersonFromMap(Map<String, Object> data, YamlParseResult result) {
        Person person = new Person();
        person.setName((String) data.get("name"));
        person.setPassportId((String) data.get("passportId"));
        
        if (data.containsKey("hairColor")) {
            Object hairColorRef = data.get("hairColor");
            Color hairColor = resolveColorReference(hairColorRef, result);
            person.setHairColor(hairColor);
        }

        if (data.containsKey("location")) {
            Object locationRef = data.get("location");
            Location location = resolveLocationReference(locationRef, result);
            person.setLocation(location);
        }

        result.getPeople().add(person);
        return person;
    }

    public String resolveEnumValueById(String type, Integer id) {
        return switch (type) {
            case "Color" -> {
                Color color = referenceRepository.findColorById(id);
                yield color != null ? color.getVal() : null;
            }
            case "Country" -> {
                Country country = referenceRepository.findCountryById(id);
                yield country != null ? country.getVal() : null;
            }
            case "Difficulty" -> {
                Difficulty difficulty = referenceRepository.findDifficultyById(id);
                yield difficulty != null ? difficulty.getVal() : null;
            }
            default -> throw new IllegalArgumentException("Unknown enum type: " + type);
        };
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