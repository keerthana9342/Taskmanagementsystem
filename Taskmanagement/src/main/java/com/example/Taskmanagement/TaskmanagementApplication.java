package com.example.taskmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.taskmanagement", "com.example.Util"})
public class taskmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(taskmanagementApplication.class, args);
	}
}
