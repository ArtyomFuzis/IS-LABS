package com.fuzis.transferdata;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChangeDTO {
    private boolean success;
    private Integer id;
    public ChangeDTO(boolean success) {
        this.success = success;
    }
}
