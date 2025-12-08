package com.fuzis.service;

import com.fuzis.entity.*;
import com.fuzis.transferdata.inner.YamlParseResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ValidationService {

    // Методы валидации для каждой сущности
    public boolean validateColor(Color color) {
        // TODO: Добавить доп. валидацию для Color
        return true;
    }

    public boolean validateCountry(Country country) {
        // TODO: Добавить доп. валидацию для Country
        return true;
    }

    public boolean validateDifficulty(Difficulty difficulty) {
        // TODO: Добавить доп. валидацию для Difficulty
        return true;
    }

    public boolean validateCoordinate(Coordinate coordinate) {
        // TODO: Добавить доп. валидацию для Coordinate
        return true;
    }

    public boolean validateLocation(Location location) {
        // TODO: Добавить доп. валидацию для Location
        return true;
    }

    public boolean validatePerson(Person person) {
        // TODO: Добавить доп. валидацию для Person
        return true;
    }

    public boolean validateDiscipline(Discipline discipline) {
        // TODO: Добавить доп. валидацию для Discipline
        return true;
    }

    public boolean validateLabWork(LabWork labWork) {
        // TODO: Добавить доп. валидацию для LabWork
        return true;
    }

    // Методы валидации зависимостей между сущностями
    public boolean validatePersonDependencies(Person person) {
        // TODO: Добавить валидацию зависимостей для Person
        return true;
    }

    public boolean validateLabWorkDependencies(LabWork labWork) {
        // TODO: Добавить валидацию зависимостей для LabWork
        return true;
    }

    // Метод для комплексной валидации всего результата парсинга
    public boolean validateYamlResult(YamlParseResult result) {
        // TODO: Добавить комплексную валидацию всего набора данных
        return true;
    }
}