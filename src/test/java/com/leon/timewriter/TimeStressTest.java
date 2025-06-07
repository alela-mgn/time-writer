package com.leon.timewriter;

import com.leon.timewriter.repository.TimeRepository;
import com.leon.timewriter.service.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class TimeStressTest {

    @Autowired
    private TimeService service;

    @Autowired
    private TimeRepository repository;

    @BeforeEach
    void clearTable() {
        repository.deleteAll();
    }

    @Test
    public void stressTestWriteTimestamp() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> service.writeTime());
        }

        // Подожди, чтобы всё завершилось
        Thread.sleep(5000);
        executor.shutdown();
    }
}
