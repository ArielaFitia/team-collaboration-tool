package com.ditto.teamcollaborationtool.repository;

import com.ditto.teamcollaborationtool.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
