package com.fuzis.util;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

@ApplicationScoped
@Getter
public class Locks {
    private final Object lock_insert_update = new Object();
}
