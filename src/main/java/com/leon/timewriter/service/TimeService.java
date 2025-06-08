package com.leon.timewriter.service;

import com.leon.timewriter.entity.Time;
import com.leon.timewriter.repository.TimeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TimeService {

    private final Logger logger = LoggerFactory.getLogger(TimeService.class);
    private final TimeRepository repository;
    private final RetryingService retryingService;
    private final Queue<Time> buffer = new LinkedList<>();
    private final ReentrantLock lock = new ReentrantLock();

    public TimeService(TimeRepository repository, RetryingService retryingService) {
        this.repository = repository;
        this.retryingService = retryingService;
    }


    @Scheduled(fixedDelay = 1000)
    public void writeTime() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        Time timeEntity = new Time(now);

        if (!retryingService.isDbAvailable()) {
            buffer.add(timeEntity);
            logger.warn("DB not available, buffered: {}", now);
            return;
        }

        lock.lock();
        try {
            while (!buffer.isEmpty()) {
                Time buffered = buffer.poll();
                try {
                    repository.save(buffered);
                    logger.debug("Saved buffered time: {}", buffered.getTimestamp());
                } catch (Exception e) {
                    logger.error("Failed to save, buffering timestamp: {}", buffered.getTimestamp(), e);
                    buffer.add(buffered);
                    retryingService.checkConnection();
                    return;
                }
            }

            try {
                repository.save(timeEntity);
                logger.debug("Saved timeEntity: {}", now);
            } catch (Exception e) {
                logger.error("Error saving current timestamp: {}", now, e);
                buffer.add(timeEntity);
                retryingService.checkConnection();
            }

        } finally {
            lock.unlock();
        }
    }

    @Transactional(readOnly = true)
    public List<Time> getAllTimestamps() {
        return repository.findAll();
    }

}
