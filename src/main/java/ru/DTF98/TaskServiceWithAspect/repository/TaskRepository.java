package ru.DTF98.TaskServiceWithAspect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.DTF98.TaskServiceWithAspect.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
