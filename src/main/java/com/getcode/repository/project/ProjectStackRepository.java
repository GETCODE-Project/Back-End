package com.getcode.repository.project;

import com.getcode.domain.project.ProjectTech;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectStackRepository extends JpaRepository<ProjectTech, Long> {
}
