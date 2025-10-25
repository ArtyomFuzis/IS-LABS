package com.fuzis.Data;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {
    boolean success;
    String reason;
    String text;
}
