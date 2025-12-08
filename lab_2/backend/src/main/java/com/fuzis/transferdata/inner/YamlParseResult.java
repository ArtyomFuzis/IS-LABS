package com.fuzis.transferdata.inner;


import com.fuzis.entity.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class YamlParseResult {
    private List<Coordinate> coordinates = new ArrayList<>();
    private List<Location> locations = new ArrayList<>();
    private List<Person> people = new ArrayList<>();
    private List<Color> colors = new ArrayList<>();
    private List<Country> countries = new ArrayList<>();
    private List<Difficulty> difficulties = new ArrayList<>();
    private List<Discipline> disciplines = new ArrayList<>();
    private List<LabWork> labWorks = new ArrayList<>();

    private transient java.util.Map<String, Object> referenceMap = new java.util.HashMap<>();
}
