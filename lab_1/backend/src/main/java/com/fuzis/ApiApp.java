package com.fuzis;

import com.fuzis.Controllers.DisciplineCRUDController;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class ApiApp extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(DisciplineCRUDController.class);
        return classes;
    }
}