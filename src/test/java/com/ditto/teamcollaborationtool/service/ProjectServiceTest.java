package com.ditto.teamcollaborationtool.service;

import com.ditto.teamcollaborationtool.dto.ProjectDTO;
import com.ditto.teamcollaborationtool.model.Project;
import com.ditto.teamcollaborationtool.model.Task;
import com.ditto.teamcollaborationtool.repository.ProjectRepository;
import com.ditto.teamcollaborationtool.repository.TaskRepository;
import com.ditto.teamcollaborationtool.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private Project project;
    private ProjectDTO projectDTO;
    private Task task;

    @BeforeEach
    void setUp() {
        project = new Project(1L, "Project A", "completed", "Project A description");
        projectDTO = new ProjectDTO(1L, "Project A", "completed", "Project A description");

        task = new Task();
        task.setId(1L);
        task.setName("Task A");
    }

    @Test
    void createProject_ShouldReturnSavedProject() {
        when(modelMapper.map(projectDTO, Project.class)).thenReturn(project);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(modelMapper.map(project, ProjectDTO.class)).thenReturn(projectDTO);

        ProjectDTO result = projectService.createProject(projectDTO);

        assertNotNull(result);
        assertEquals(project.getName(), result.getName());
    }

    @Test
    void getProjectById_ShouldReturnProject_WhenProjectExists() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(modelMapper.map(project, ProjectDTO.class)).thenReturn(projectDTO);

        ProjectDTO result = projectService.getProjectById(1L);

        assertNotNull(result);
        assertEquals(project.getName(), result.getName());
        assertEquals(project.getDescription(), result.getDescription());
    }

    @Test
    void getAllProjects_ShouldReturnAllProjects() {
        Project projectB = new Project(1L, "Project B", "not completed", "Project B description");
        ProjectDTO projectDTOB = new ProjectDTO(1L, "Project B", "not completed", "Project B description");
        List<Project> projects = Arrays.asList(project, projectB);

        when(projectRepository.findAll()).thenReturn(projects);
        when(modelMapper.map(project, ProjectDTO.class)).thenReturn(projectDTO);
        when(modelMapper.map(projectB, ProjectDTO.class)).thenReturn(projectDTOB);

        List<ProjectDTO> result = projectService.getAllProjects();

        assertNotNull(result);
        assertEquals(projects.size(), result.size());
        assertEquals(project.getName(), result.get(0).getName());
        assertEquals(projectB.getName(), result.get(1).getName());
    }

    @Test
    void updateProject_ShouldReturnUpdatedProject_WhenProjectExists() {
        Long projectId = 1L;
        ProjectDTO updateRequest = new ProjectDTO(projectId, "Updated project", "completed", "Updated project description");
        Project existingProject = new Project(projectId, "Old project", "not completed", "Old project description");
        Project updatedProject = new Project(projectId, "Updated project", "completed", "Updated project description");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);
        when(modelMapper.map(updatedProject, ProjectDTO.class)).thenReturn(updateRequest);

        ProjectDTO result = projectService.updateProject(projectId, updateRequest);

        assertNotNull(result);
        assertEquals(updateRequest.getName(), result.getName());
        assertEquals(updateRequest.getDescription(), result.getDescription());
    }

    @Test
    void deleteProject_ShouldCallRepository() {
        projectService.deleteProject(1L);

        verify(projectRepository).deleteById(1L);
    }

    @Test
    void addTaskToProject_ShouldReturnUpdatedProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(modelMapper.map(project, ProjectDTO.class)).thenReturn(projectDTO);

        ProjectDTO result = projectService.addTaskToProject(1L, 1L);

        assertNotNull(result);
        verify(projectRepository).save(project);
        verify(taskRepository).save(task);
    }

    @Test
    void removeTaskFromProject_ShouldReturnUpdatedProject() {
        project.getTasks().add(task);
        task.setProject(project);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(modelMapper.map(project, ProjectDTO.class)).thenReturn(projectDTO);

        ProjectDTO result = projectService.deleteTaskFromProject(1L, 1L);

        assertNotNull(result);
        verify(projectRepository).save(project);
        verify(taskRepository).save(task);
    }
}
