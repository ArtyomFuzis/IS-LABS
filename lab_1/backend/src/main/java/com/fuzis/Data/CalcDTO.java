package com.fuzis.Data;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CalcDTO<T> {
    private boolean success;
    private T result;
}
