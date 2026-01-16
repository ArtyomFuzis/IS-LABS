package com.fuzis.service;

import com.fuzis.database.EntityMangerCreator;
import com.fuzis.util.JtaConfiguration;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped

public class CacheStatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(CacheStatisticsService.class);
    @Getter

    private Statistics statistics;

    @Getter
    private boolean loggingEnabled = false;

    @Inject
    private JtaConfiguration jtaConfiguration;

    @PostConstruct
    public void init() {
        SessionFactory sessionFactory = jtaConfiguration.createEntityManagerFactory().unwrap(SessionFactory.class);
        this.statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);
        logger.info("Cache statistics service initialized");
    }

    public void enableLogging() {
        this.loggingEnabled = true;
        logger.info("Cache statistics logging ENABLED");
    }

    public void disableLogging() {
        this.loggingEnabled = false;
        logger.info("Cache statistics logging DISABLED");
    }

    public void logStatistics(String operation) {
        if (!loggingEnabled) return;

        logger.info("=== Cache Statistics for {} ===", operation);
        logger.info("L2 Cache Hits: {}", statistics.getSecondLevelCacheHitCount());
        logger.info("L2 Cache Misses: {}", statistics.getSecondLevelCacheMissCount());
        logger.info("L2 Cache Puts: {}", statistics.getSecondLevelCachePutCount());
        logger.info("Query Cache Hits: {}", statistics.getQueryCacheHitCount());
        logger.info("Query Cache Misses: {}", statistics.getQueryCacheMissCount());

        long l2Total = statistics.getSecondLevelCacheHitCount() +
                statistics.getSecondLevelCacheMissCount();
        if (l2Total > 0) {
            double hitRatio = (double) statistics.getSecondLevelCacheHitCount() / l2Total;
            // Исправляем форматирование
            logger.info("L2 Cache Hit Ratio: {}%", String.format("%.2f", hitRatio * 100));
        } else {
            logger.info("L2 Cache Hit Ratio: N/A (no requests)");
        }
        logger.info("==============================");
    }

}