package com.ditto.teamcollaborationtool.service;

import com.ditto.teamcollaborationtool.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {
    ProjectDTO createProject(ProjectDTO projectDTO);
    ProjectDTO getProjectById(Long id);
    List<ProjectDTO> getAllProjects();
    ProjectDTO updateProject(Long id, ProjectDTO projectDTO);
    void deleteProject(Long id);
    ProjectDTO addTaskToProject(Long projectId, Long taskId);
    ProjectDTO deleteTaskFromProject(Long projectId, Long taskId);
}
