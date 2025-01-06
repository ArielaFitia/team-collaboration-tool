package com.ditto.teamcollaborationtool.controller;

import com.ditto.teamcollaborationtool.dto.ProjectDTO;
import com.ditto.teamcollaborationtool.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDTO createProject(@RequestBody ProjectDTO projectDTO) {
        return projectService.createProject(projectDTO);
    }

    @GetMapping
    public List<ProjectDTO> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ProjectDTO getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PutMapping("/{id}")
    public ProjectDTO updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        return projectService.updateProject(id, projectDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }

    @PostMapping("{projectId}/tasks/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO addTaskToProject(@PathVariable Long projectId, @PathVariable Long taskId) {
        return projectService.addTaskToProject(projectId, taskId);
    }

    @DeleteMapping("{projectId}/tasks/{taskId}")
    public ProjectDTO deleteTaskFromProject(@PathVariable Long projectId, @PathVariable Long taskId) {
        return projectService.deleteTaskFromProject(projectId, taskId);
    }
}

