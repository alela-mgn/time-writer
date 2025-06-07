package com.leon.timewriter.controller;

import com.leon.timewriter.entity.Time;
import com.leon.timewriter.service.TimeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/timestamps")
public class TimeController {

    private final TimeService service;

    public TimeController(TimeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Time> getAll() {
        return service.getAllTimestamps();
    }
}
