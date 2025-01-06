package com.ditto.teamcollaborationtool.controller;

import com.ditto.teamcollaborationtool.dto.TeamDTO;
import com.ditto.teamcollaborationtool.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamDTO createTeam(@RequestBody TeamDTO teamDTO) {
        return teamService.createTeam(teamDTO);
    }

    @GetMapping
    public List<TeamDTO> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public TeamDTO getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    @PutMapping("/{id}")
    public TeamDTO updateTeam(@PathVariable Long id, @RequestBody TeamDTO teamDTO) {
        return teamService.updateTeam(id, teamDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
    }

    @PostMapping("/{teamId}/members/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public TeamDTO addMemberToTeam(@PathVariable Long teamId, @PathVariable Long memberId) {
        return teamService.addMemberToTeam(teamId, memberId);
    }

    @DeleteMapping("/{teamId}/members/{memberId}")
    public TeamDTO deleteMemberFromTeam(@PathVariable Long teamId, @PathVariable Long memberId) {
        return teamService.removeMemberFromTeam(teamId, memberId);
    }

    @PostMapping("/{teamId}/projects/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public TeamDTO addProjectToTeam(@PathVariable Long teamId, @PathVariable Long projectId) {
        return teamService.addProjectToTeam(teamId, projectId);
    }

    @DeleteMapping("/{teamId}/projects/{projectId}")
    public TeamDTO deleteProjectFromTeam(@PathVariable Long teamId, @PathVariable Long projectId) {
        return teamService.removeProjectFromTeam(teamId, projectId);
    }
}
