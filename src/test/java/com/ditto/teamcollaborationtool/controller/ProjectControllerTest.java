package com.ditto.teamcollaborationtool.controller;

import com.ditto.teamcollaborationtool.dto.ProjectDTO;
import com.ditto.teamcollaborationtool.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest {
    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController controller;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ProjectDTO testProjectDTO;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        testProjectDTO = new ProjectDTO();
        testProjectDTO.setId(1L);
        testProjectDTO.setName("test project");
        testProjectDTO.setStatus("active");
        testProjectDTO.setDescription("test description");
    }

    @Test
    void whenCreateProject_thenReturnCreatedProject() throws Exception {
        when(projectService.createProject(any(ProjectDTO.class))).thenReturn(testProjectDTO);

        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProjectDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testProjectDTO.getId()))
                .andExpect(jsonPath("$.name").value(testProjectDTO.getName()))
                .andExpect(jsonPath("$.status").value(testProjectDTO.getStatus()))
                .andExpect(jsonPath("$.description").value(testProjectDTO.getDescription()));
    }

    @Test
    void whenGetAllProjects_thenReturnAllProjects() throws Exception {
        List<ProjectDTO> projects = Arrays.asList(testProjectDTO);
        when(projectService.getAllProjects()).thenReturn(projects);

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testProjectDTO.getId()))
                .andExpect(jsonPath("$[0].name").value(testProjectDTO.getName()))
                .andExpect(jsonPath("$[0].status").value(testProjectDTO.getStatus()))
                .andExpect(jsonPath("$[0].description").value(testProjectDTO.getDescription()));
    }

    @Test
    void whenGetProjectById_thenReturnProject() throws Exception {
        when(projectService.getProjectById(1L)).thenReturn(testProjectDTO);

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testProjectDTO.getId()))
                .andExpect(jsonPath("$.name").value(testProjectDTO.getName()))
                .andExpect(jsonPath("$.status").value(testProjectDTO.getStatus()))
                .andExpect(jsonPath("$.description").value(testProjectDTO.getDescription()));
    }

    @Test
    void whenUpdateProject_thenReturnUpdatedProject() throws Exception {
        when(projectService.updateProject(eq(1L), any(ProjectDTO.class))).thenReturn(testProjectDTO);

        mockMvc.perform(put("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProjectDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testProjectDTO.getId()))
                .andExpect(jsonPath("$.name").value(testProjectDTO.getName()))
                .andExpect(jsonPath("$.status").value(testProjectDTO.getStatus()))
                .andExpect(jsonPath("$.description").value(testProjectDTO.getDescription()));
    }

    @Test
    void whenDeleteProject_thenReturnDeletedProject() throws Exception {
        doNothing().when(projectService).deleteProject(eq(1L));

        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isNoContent());
    }
}
