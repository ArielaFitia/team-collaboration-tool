package com.ditto.teamcollaborationtool.service.impl;

import com.ditto.teamcollaborationtool.dto.TeamDTO;
import com.ditto.teamcollaborationtool.model.Member;
import com.ditto.teamcollaborationtool.model.Project;
import com.ditto.teamcollaborationtool.model.Team;
import com.ditto.teamcollaborationtool.repository.MemberRepository;
import com.ditto.teamcollaborationtool.repository.ProjectRepository;
import com.ditto.teamcollaborationtool.repository.TeamRepository;
import com.ditto.teamcollaborationtool.service.TeamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceimpl implements TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public TeamServiceimpl(TeamRepository teamRepository, ModelMapper modelMapper, MemberRepository memberRepository, ProjectRepository projectRepository) {
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
        this.memberRepository = memberRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public TeamDTO createTeam(TeamDTO teamDTO) {
        Team team = modelMapper.map(teamDTO, Team.class);
        team = teamRepository.save(team);
        return modelMapper.map(team, TeamDTO.class);
    }

    @Override
    public TeamDTO getTeamById(Long id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new RuntimeException("Team not found"));
        return modelMapper.map(team, TeamDTO.class);
    }

    @Override
    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(team -> modelMapper.map(team, TeamDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public TeamDTO updateTeam(Long id, TeamDTO teamDTO) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new RuntimeException("Team not found"));
        team.setName(teamDTO.getName());
        team.setDescription(teamDTO.getDescription());
        team = teamRepository.save(team);
        return modelMapper.map(team, TeamDTO.class);
    }

    @Override
    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }

    @Override
    public TeamDTO addMemberToTeam(Long teamId, Long memberId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));

        member.setTeam(team);
        if(team.getMembers() == null) {
            team.setMembers(new ArrayList<>());
        }
        team.getMembers().add(member);

        memberRepository.save(member);
        Team updatedTeam = teamRepository.save(team);

        return modelMapper.map(updatedTeam, TeamDTO.class);
    }

    @Override
    public TeamDTO removeMemberFromTeam(Long teamId, Long memberId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));

        member.setTeam(null);                   // Remove team reference from member
        if (team.getMembers() != null) {
            team.getMembers().remove(member);    // Remove member from team's list
        }
        memberRepository.save(member);
        Team updatedTeam = teamRepository.save(team);

        return modelMapper.map(updatedTeam, TeamDTO.class);
    }

    @Override
    public TeamDTO addProjectsToTeam(Long teamId, Long projectId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));

        project.setTeam(team);
        if(team.getProjects() == null) {
            team.setProjects(new ArrayList<>());
        }
        team.getProjects().add(project);

        projectRepository.save(project);
        Team updatedTeam = teamRepository.save(team);

        return modelMapper.map(updatedTeam, TeamDTO.class);
    }

    @Override
    public TeamDTO removeProjectFromTeam(Long teamId, Long projectId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));

        project.setTeam(null);
        if(team.getProjects() != null) {
            team.getProjects().remove(project);
        }

        projectRepository.save(project);
        Team updatedTeam = teamRepository.save(team);

        return modelMapper.map(updatedTeam, TeamDTO.class);
    }
}
