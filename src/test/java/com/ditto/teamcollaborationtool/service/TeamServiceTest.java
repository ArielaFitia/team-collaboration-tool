package com.ditto.teamcollaborationtool.service;

import com.ditto.teamcollaborationtool.dto.TeamDTO;
import com.ditto.teamcollaborationtool.model.Member;
import com.ditto.teamcollaborationtool.model.Project;
import com.ditto.teamcollaborationtool.model.Team;
import com.ditto.teamcollaborationtool.repository.MemberRepository;
import com.ditto.teamcollaborationtool.repository.ProjectRepository;
import com.ditto.teamcollaborationtool.repository.TeamRepository;
import com.ditto.teamcollaborationtool.service.impl.TeamServiceimpl;
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
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TeamServiceimpl teamService;

    private Team team;
    private TeamDTO teamDTO;
    private Member member;
    private Project project;

    @BeforeEach
    void setUp() {
        team = new Team(1L, "Team A", "Description of Team A");
        teamDTO = new TeamDTO(1L, "Team A", "Description of Team A");

        member = new Member();
        member.setId(1L);
        member.setName("Member A");

        project = new Project();
        project.setId(1L);
        project.setName("Project A");
    }

    @Test
    void createTeam_ShouldReturnSavedTeam() {
        when(modelMapper.map(teamDTO, Team.class)).thenReturn(team);
        when(teamRepository.save(any(Team.class))).thenReturn(team);
        when(modelMapper.map(team, TeamDTO.class)).thenReturn(teamDTO);

        TeamDTO result = teamService.createTeam(teamDTO);

        assertNotNull(result);
        assertEquals(team.getName(), result.getName());
    }

    @Test
    void getTeamById_ShouldReturnTeam_WhenTeamExists() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(modelMapper.map(team, TeamDTO.class)).thenReturn(teamDTO);

        TeamDTO result = teamService.getTeamById(1L);

        assertNotNull(result);
        assertEquals(team.getName(), result.getName());
        assertEquals(team.getDescription(), result.getDescription());
    }

    @Test
    void getAllTeams_ShouldReturnListOfTeams() {

        Team teamB = new Team(2L, "Team B", "Description of Team B");
        TeamDTO teamDTOB = new TeamDTO(2L, "Team B", "Description of Team B");
        List<Team> teams = Arrays.asList(team, teamB);

        when(teamRepository.findAll()).thenReturn(teams);
        when(modelMapper.map(team, TeamDTO.class)).thenReturn(teamDTO);
        when(modelMapper.map(teamB, TeamDTO.class)).thenReturn(teamDTOB);

        List<TeamDTO> result = teamService.getAllTeams();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Team A", result.get(0).getName());
        assertEquals("Team B", result.get(1).getName());
    }

    @Test
    void updateTeam_ShouldReturnUpdatedTeam() {
        Long teamId = 1L;
        TeamDTO updateRequest = new TeamDTO(teamId, "Updated Team", "Updated Description");
        Team existingTeam = new Team(teamId, "Old Team", "Old Description");
        Team updatedTeam = new Team(teamId, "Updated Team", "Updated Description");

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));
        when(teamRepository.save(any(Team.class))).thenReturn(updatedTeam);
        when(modelMapper.map(updatedTeam, TeamDTO.class)).thenReturn(updateRequest);

        TeamDTO result = teamService.updateTeam(teamId, updateRequest);

        assertNotNull(result);
        assertEquals(updateRequest.getName(), result.getName());
    }

    @Test
    void deleteTeam_ShouldReturnDeletedTeam() {
        teamRepository.deleteById(1L);

        verify(teamRepository).deleteById(1L);
    }

    @Test
    void addMemberToTeam_ShouldReturnUpdatedTeam() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(teamRepository.save(any(Team.class))).thenReturn(team);
        when(modelMapper.map(team, TeamDTO.class)).thenReturn(teamDTO);

        TeamDTO result = teamService.addMemberToTeam(1L, 1L);

        assertNotNull(result);
        verify(teamRepository).save(team);
        verify(memberRepository).save(member);
    }

    @Test
    void removeMemberFromTeam_ShouldReturnUpdatedTeam() {
        team.getMembers().add(member);
        member.setTeam(team);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(teamRepository.save(any(Team.class))).thenReturn(team);
        when(modelMapper.map(team, TeamDTO.class)).thenReturn(teamDTO);

        TeamDTO result = teamService.removeMemberFromTeam(1L, 1L);

        assertNotNull(result);
        verify(teamRepository).save(team);
        verify(memberRepository).save(member);
    }

    @Test
    void addProjectToTeam_ShouldReturnUpdatedTeam() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(teamRepository.save(any(Team.class))).thenReturn(team);
        when(modelMapper.map(team, TeamDTO.class)).thenReturn(teamDTO);

        TeamDTO result = teamService.addProjectToTeam(1L, 1L);

        assertNotNull(result);
        verify(teamRepository).save(team);
        verify(projectRepository).save(project);
    }

    @Test
    void removeProjectFromTeam_ShouldReturnUpdatedTeam() {
        team.getProjects().add(project);
        project.setTeam(team);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(teamRepository.save(any(Team.class))).thenReturn(team);
        when(modelMapper.map(team, TeamDTO.class)).thenReturn(teamDTO);

        TeamDTO result = teamService.removeProjectFromTeam(1L, 1L);

        assertNotNull(result);
        verify(teamRepository).save(team);
        verify(projectRepository).save(project);
    }
}
