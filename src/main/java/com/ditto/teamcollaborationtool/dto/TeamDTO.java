package com.ditto.teamcollaborationtool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTO {
    private Long id;
    private String name;
    private String description;
    private List<MemberDTO> members; // Optional nesting
    private List<ProjectDTO> projects; // Optional nesting
}

