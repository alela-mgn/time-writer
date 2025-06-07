package com.leon.timewriter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TimeWriterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeWriterApplication.class, args);
	}

}
