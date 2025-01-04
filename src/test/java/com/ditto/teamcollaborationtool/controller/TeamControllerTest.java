package com.ditto.teamcollaborationtool.controller;

import com.ditto.teamcollaborationtool.dto.TeamDTO;
import com.ditto.teamcollaborationtool.service.TeamService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class TeamControllerTest {
    @Mock
    private TeamService teamService;

    @InjectMocks
    private TeamController teamController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private TeamDTO testTeamDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(teamController)
                .build();

        testTeamDTO = new TeamDTO();
        testTeamDTO.setId(1L);
        testTeamDTO.setName("Test Team");
        testTeamDTO.setDescription("Test Description");
    }

    @Test
    void whenCreateTeam_thenReturnCreatedTeam() throws Exception {
        when(teamService.createTeam(any(TeamDTO.class))).thenReturn(testTeamDTO);

        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTeamDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testTeamDTO.getId()))
                .andExpect(jsonPath("$.name").value(testTeamDTO.getName()))
                .andExpect(jsonPath("$.description").value(testTeamDTO.getDescription()));
    }

    @Test
    void whenGetAllTeams_thenReturnTeamsList() throws Exception {
        List<TeamDTO> teams = Arrays.asList(testTeamDTO);
        when(teamService.getAllTeams()).thenReturn(teams);

        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testTeamDTO.getId()))
                .andExpect(jsonPath("$[0].name").value(testTeamDTO.getName()));
    }

    @Test
    void whenGetTeamById_thenReturnTeam() throws Exception {
        when(teamService.getTeamById(1L)).thenReturn(testTeamDTO);

        mockMvc.perform(get("/api/teams/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testTeamDTO.getId()))
                .andExpect(jsonPath("$.name").value(testTeamDTO.getName()));
    }

    @Test
    void whenUpdateTeam_thenReturnUpdatedTeam() throws Exception {
        when(teamService.updateTeam(eq(1L), any(TeamDTO.class))).thenReturn(testTeamDTO);

        mockMvc.perform(put("/api/teams/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTeamDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testTeamDTO.getId()))
                .andExpect(jsonPath("$.name").value(testTeamDTO.getName()));
    }

    @Test
    void whenDeleteTeam_thenReturnNoContent() throws Exception {
        doNothing().when(teamService).deleteTeam(1L);

        mockMvc.perform(delete("/api/teams/1"))
                .andExpect(status().isNoContent());
    }
}
