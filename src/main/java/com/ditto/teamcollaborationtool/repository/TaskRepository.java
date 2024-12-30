package com.ditto.teamcollaborationtool.repository;

import com.ditto.teamcollaborationtool.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
