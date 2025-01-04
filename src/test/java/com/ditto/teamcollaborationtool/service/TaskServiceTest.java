package com.ditto.teamcollaborationtool.service;

import com.ditto.teamcollaborationtool.dto.TaskDTO;
import com.ditto.teamcollaborationtool.model.Task;
import com.ditto.teamcollaborationtool.repository.TaskRepository;
import com.ditto.teamcollaborationtool.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        task = new Task(1L, "Task A", "Description of Task A", LocalDate.of(2019, 8, 6), "completed");
        taskDTO = new TaskDTO(1L, "Task A", "Description of Task A", LocalDate.of(2019, 8, 6), "completed");
    }

    @Test
    void createTask_shouldReturnSavedTask() {
        when(modelMapper.map(taskDTO, Task.class)).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(modelMapper.map(task, TaskDTO.class)).thenReturn(taskDTO);

        TaskDTO result = taskService.createTask(taskDTO);

        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
    }

    @Test
    void getTaskById_shouldReturnTask_WhenTaskIsFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(modelMapper.map(task, TaskDTO.class)).thenReturn(taskDTO);

        TaskDTO result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(task.getName(), result.getName());
        assertEquals(task.getDescription(), result.getDescription());
    }

    @Test
    void getAllTasks_shouldReturnAllTasks() {
        Task taskB = new Task(2L, "TaskB", "Description of TaskB", LocalDate.of(2019, 8, 7), "not completed");
        TaskDTO taskDTOB =  new TaskDTO(2L, "TaskB", "Description of TaskB", LocalDate.of(2019, 8, 7), "not completed");
        List<Task> tasks = Arrays.asList(task, taskB);

        when(taskRepository.findAll()).thenReturn(tasks);
        when(modelMapper.map(task, TaskDTO.class)).thenReturn(taskDTO);
        when(modelMapper.map(taskB, TaskDTO.class)).thenReturn(taskDTOB);

        List<TaskDTO> result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(tasks.size(), result.size());
        assertEquals(taskDTO.getName(), result.get(0).getName());
        assertEquals(taskDTOB.getName(), result.get(1).getName());
    }

    @Test
    void updateTask_shouldReturnUpdatedTask_WhenTaskIsFound() {
        Long taskId = 1L;
        TaskDTO updateRequest = new TaskDTO(taskId, "Updated Task", "Updated description", LocalDate.of(2020, 8, 6), "completed");
        Task existingTask = new Task(taskId, "Old Task", "Old description", LocalDate.of(2020, 9, 6), "not completed");
        Task updatedTask = new Task(taskId, "Updated Task", "Updated description", LocalDate.of(2020, 8, 6), "completed");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(modelMapper.map(updatedTask, TaskDTO.class)).thenReturn(updateRequest);

        TaskDTO result = taskService.updateTask(taskId, updateRequest);

        assertNotNull(result);
        assertEquals(updateRequest.getName(), result.getName());
        assertEquals(updateRequest.getDescription(), result.getDescription());
    }

    @Test
    void deleteTask_shouldCallRepository() {
        taskRepository.deleteById(1L);

        verify(taskRepository).deleteById(1L);
    }
}
