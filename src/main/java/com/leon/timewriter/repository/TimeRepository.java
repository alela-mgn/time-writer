package com.leon.timewriter.repository;

import com.leon.timewriter.entity.Time;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeRepository extends JpaRepository<Time, Long> {
}
