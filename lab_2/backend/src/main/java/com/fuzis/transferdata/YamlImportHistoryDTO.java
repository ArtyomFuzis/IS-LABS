package com.fuzis.transferdata;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class YamlImportHistoryDTO {
    private Integer id;
    private ZonedDateTime time;
    private String status;
    private Integer importedObjects;
    private String errorMessage;
}