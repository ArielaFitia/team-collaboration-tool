package com.ditto.teamcollaborationtool.controller;

import com.ditto.teamcollaborationtool.dto.TaskDTO;
import com.ditto.teamcollaborationtool.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
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
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private TaskDTO testTaskDTO;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        testTaskDTO = new TaskDTO();
        testTaskDTO.setId(1L);
        testTaskDTO.setName("Test Task");
        testTaskDTO.setDescription("Test Task Description");
        testTaskDTO.setDueDate(LocalDate.of(2020, 10, 10));
        testTaskDTO.setStatus("Active");
    }

    @Test
    void whenCreateTask_thenReturnCreatedTask() throws Exception {
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(testTaskDTO);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTaskDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testTaskDTO.getId()))
                .andExpect(jsonPath("$.name").value(testTaskDTO.getName()))
                .andExpect(jsonPath("$.description").value(testTaskDTO.getDescription()))
                .andExpect(jsonPath("$.dueDate[0]").value(2020))
                .andExpect(jsonPath("$.dueDate[1]").value(10))
                .andExpect(jsonPath("$.dueDate[2]").value(10));
    }

    @Test
    void whenGetAllTasks_thenReturnAllTasks() throws Exception {
        List<TaskDTO> tasks = Arrays.asList(testTaskDTO);
        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testTaskDTO.getId()))
                .andExpect(jsonPath("$[0].name").value(testTaskDTO.getName()))
                .andExpect(jsonPath("$[0].description").value(testTaskDTO.getDescription()))
                .andExpect(jsonPath("$[0].dueDate[0]").value(2020))
                .andExpect(jsonPath("$[0].dueDate[1]").value(10))
                .andExpect(jsonPath("$[0].dueDate[2]").value(10));
    }

    @Test
    void whenGetTaskById_thenReturnTask() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(testTaskDTO);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testTaskDTO.getId()))
                .andExpect(jsonPath("$.name").value(testTaskDTO.getName()))
                .andExpect(jsonPath("$.description").value(testTaskDTO.getDescription()))
                .andExpect(jsonPath("$.dueDate[0]").value(2020))
                .andExpect(jsonPath("$.dueDate[1]").value(10))
                .andExpect(jsonPath("$.dueDate[2]").value(10));
    }

    @Test
    void whenUpdateTask_thenReturnUpdatedTask() throws Exception {
        when(taskService.updateTask(eq(1L), any(TaskDTO.class))).thenReturn(testTaskDTO);

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTaskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testTaskDTO.getId()))
                .andExpect(jsonPath("$.name").value(testTaskDTO.getName()))
                .andExpect(jsonPath("$.description").value(testTaskDTO.getDescription()))
                .andExpect(jsonPath("$.dueDate[0]").value(2020))
                .andExpect(jsonPath("$.dueDate[1]").value(10))
                .andExpect(jsonPath("$.dueDate[2]").value(10));
    }

    @Test
    void whenDeleteTask_thenReturnDeletedTask() throws Exception {
        doNothing().when(taskService).deleteTask(eq(1L));

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
