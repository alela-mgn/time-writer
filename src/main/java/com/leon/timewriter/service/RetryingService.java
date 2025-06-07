package com.leon.timewriter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class RetryingService {
    private static final Logger logger = LoggerFactory.getLogger(RetryingService.class);
    private final AtomicBoolean dbAvailable = new AtomicBoolean(true);
    private final DataSource dataSource;

    public RetryingService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isDbAvailable() {
        return dbAvailable.get();
    }

    @Scheduled(fixedDelay = 5000)
    public void checkConnection() {
        try (Connection connection = dataSource.getConnection()) {
            dbAvailable.set(connection.isValid(2));
        } catch (Exception e) {
            dbAvailable.set(false);
            logger.warn("DB connection check failed: {}", e.getMessage());
        }
    }

}
