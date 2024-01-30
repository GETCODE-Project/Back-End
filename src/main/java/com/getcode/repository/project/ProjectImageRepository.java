package com.getcode.repository.project;

import com.getcode.domain.project.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {
    List<ProjectImage> findAllByProjectId(Long id);
}
