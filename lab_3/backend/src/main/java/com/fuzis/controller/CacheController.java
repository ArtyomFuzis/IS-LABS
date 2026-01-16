package com.fuzis.controller;

import com.fuzis.service.CacheStatisticsService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.stat.Statistics;

import java.util.HashMap;
import java.util.Map;

@Path("/cache")
public class CacheController {

    @Inject
    private CacheStatisticsService cacheStatisticsService;

    @POST
    @Path("/logging/{enabled}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response toggleLogging(@PathParam("enabled") boolean enabled) {
        if (enabled) {
            cacheStatisticsService.enableLogging();
        } else {
            cacheStatisticsService.disableLogging();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("loggingEnabled", enabled);
        response.put("message", "Cache statistics logging " + (enabled ? "enabled" : "disabled"));

        return Response.ok(response).build();
    }

    @GET
    @Path("/statistics")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatistics() {
        Statistics stats = cacheStatisticsService.getStatistics();

        Map<String, Object> statistics = new HashMap<>();

        statistics.put("l2CacheHits", stats.getSecondLevelCacheHitCount());
        statistics.put("l2CacheMisses", stats.getSecondLevelCacheMissCount());
        statistics.put("l2CachePuts", stats.getSecondLevelCachePutCount());

        statistics.put("queryCacheHits", stats.getQueryCacheHitCount());
        statistics.put("queryCacheMisses", stats.getQueryCacheMissCount());
        statistics.put("queryCachePuts", stats.getQueryCachePutCount());

        statistics.put("entityDeleteCount", stats.getEntityDeleteCount());
        statistics.put("entityInsertCount", stats.getEntityInsertCount());
        statistics.put("entityLoadCount", stats.getEntityLoadCount());
        statistics.put("entityFetchCount", stats.getEntityFetchCount());
        statistics.put("entityUpdateCount", stats.getEntityUpdateCount());

        long l2Total = stats.getSecondLevelCacheHitCount() + stats.getSecondLevelCacheMissCount();
        if (l2Total > 0) {
            statistics.put("l2CacheHitRatio",
                    String.format("%.2f%%", (double) stats.getSecondLevelCacheHitCount() / l2Total * 100));
        }

        statistics.put("loggingEnabled", cacheStatisticsService.isLoggingEnabled());
        statistics.put("statisticsEnabled", stats.isStatisticsEnabled());

        return Response.ok(statistics).build();
    }

    @POST
    @Path("/clear")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearCache() {
        Statistics stats = cacheStatisticsService.getStatistics();
        stats.clear();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Cache statistics cleared");

        return Response.ok(response).build();
    }
}