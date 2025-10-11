package com.fuzis.Services;

import com.fuzis.Annotations.Filterable;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class UtilityService {
    public <T> List<String> getFilterableFields (Class<T> cls){
        return Arrays.stream(cls.getDeclaredFields())
                .filter(x -> x.isAnnotationPresent(Filterable.class))
                .map(x-> x.getName() + x.getAnnotation(Filterable.class).addition())
                .toList();
    }
}
