package com.leon.timewriter.controller;

import com.leon.timewriter.entity.Time;
import com.leon.timewriter.service.TimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/timestamps")
@Tag(name = "Timestamps", description = "Операции с временными метками")
public class TimeController {

    private final TimeService service;

    public TimeController(TimeService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Получить все временные метки")
    public List<Time> getAll() {
        return service.getAllTimestamps();
    }
}
