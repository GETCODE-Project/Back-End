package com.getcode.repository.project;

import com.getcode.domain.project.ProjectTech;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectStackRepository extends JpaRepository<ProjectTech, Long> {
    ProjectTech findByProjectId(Long id);

    List<ProjectTech> findAllByProjectId(Long id);
}
