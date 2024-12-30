package com.ditto.teamcollaborationtool.repository;

import com.ditto.teamcollaborationtool.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
