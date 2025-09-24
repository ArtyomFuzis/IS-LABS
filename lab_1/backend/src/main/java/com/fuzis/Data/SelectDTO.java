package com.fuzis.Data;

import com.fuzis.Entities.Discipline;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SelectDTO<T>{
    private boolean success;
    private List<T> discipline;
}
