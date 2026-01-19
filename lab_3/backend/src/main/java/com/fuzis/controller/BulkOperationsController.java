package com.fuzis.controller;

import com.fuzis.service.YamlImportService;
import com.fuzis.transferdata.ChangeDTO;
import com.fuzis.transferdata.ErrorDTO;
import com.fuzis.transferdata.YamlImportHistoryDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;

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
    public Response bulkImportYaml(String yamlContent) throws Exception {
        if (yamlContent == null || yamlContent.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorDTO(false, "error", "YAML content is empty"))
                    .build();
        }

        byte[] bytes = yamlContent.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        yamlImportService.importYaml(inputStream, bytes.length);

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

    @GET
    @Path("/download/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@PathParam("id") Integer id) {
        try {
            YamlImportService.FileDownloadResult result = yamlImportService.downloadImportedFile(id);

            StreamingOutput stream = output -> {
            result.getInputStream().transferTo(output);

            };

            return Response.ok(stream)
                    .header("Content-Disposition",
                            "attachment; filename=\"" + result.getFilename() + "\"")
                    .header("Content-Type", "application/octet-stream")
                    .build();

        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ошибка при скачивании файла: " + e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }
}