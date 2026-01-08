package com.fuzis.controller;

import com.fuzis.service.YamlImportService;
import com.fuzis.transferdata.ChangeDTO;
import com.fuzis.transferdata.ErrorDTO;
import com.fuzis.transferdata.YamlImportHistoryDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

@Path("/operations/bulk")
public class BulkOperationsController {

    @Inject
    private YamlImportService yamlImportService;

    @POST
    @Path("/")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response bulkImportYaml(String yamlContent) {
        if (yamlContent == null || yamlContent.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorDTO(false, "error", "YAML content is empty"))
                    .build();
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(yamlContent.getBytes());
        yamlImportService.importYaml(inputStream);

        return Response.ok(new ChangeDTO(true)).build();
    }

    @GET
    @Path("/history")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImportHistory() {
        try {
            List<YamlImportHistoryDTO> history = yamlImportService.getImportHistory(20)
                    .stream()
                    .map(record -> new YamlImportHistoryDTO(
                            record.getId(),
                            record.getTime(),
                            record.getStatus(),
                            record.getImportedObjects(),
                            record.getErrorMessage()
                    ))
                    .collect(Collectors.toList());

            return Response.ok(history).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorDTO(false, "error", "Failed to retrieve import history: " + e.getMessage()))
                    .build();
        }
    }
}