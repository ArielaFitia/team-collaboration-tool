package com.ditto.teamcollaborationtool.service.impl;

import com.ditto.teamcollaborationtool.dto.ProjectDTO;
import com.ditto.teamcollaborationtool.dto.TaskDTO;
import com.ditto.teamcollaborationtool.model.Project;
import com.ditto.teamcollaborationtool.model.Task;
import com.ditto.teamcollaborationtool.repository.ProjectRepository;
import com.ditto.teamcollaborationtool.repository.TaskRepository;
import com.ditto.teamcollaborationtool.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;
    private final TaskRepository taskRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, ModelMapper modelMapper, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        this.taskRepository = taskRepository;
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = modelMapper.map(projectDTO, Project.class);
        project = projectRepository.save(project);
        return modelMapper.map(project, ProjectDTO.class);
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        return modelMapper.map(project, ProjectDTO.class);
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(project -> modelMapper.map(project, ProjectDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project = projectRepository.save(project);
        return modelMapper.map(project, ProjectDTO.class);
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public ProjectDTO addTaskToProject(Long projectId, Long taskId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        task.setProject(project);
        if(project.getTasks() == null) {
            project.setTasks(new ArrayList<>());
        }
        project.getTasks().add(task);

        taskRepository.save(task);
        Project updatedProject = projectRepository.save(project);
        return modelMapper.map(updatedProject, ProjectDTO.class);
    }

    @Override
    public ProjectDTO deleteTaskFromProject(Long projectId, Long taskId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        task.setProject(null);
        if(project.getTasks() != null) {
            project.getTasks().remove(task);
        }
        taskRepository.save(task);
        Project updatedProject = projectRepository.save(project);
        return modelMapper.map(updatedProject, ProjectDTO.class);
    }
}
