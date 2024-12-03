package ru.DTF98.TaskServiceWithAspect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TaskServiceWithAspectApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskServiceWithAspectApplication.class, args);
	}
}
