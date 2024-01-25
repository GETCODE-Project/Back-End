package com.getcode.repository.project;

import com.getcode.domain.project.ProjectSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectSubjectRepository extends JpaRepository<ProjectSubject, Long> {
    ProjectSubject findByProjectId(Long id);

    List<ProjectSubject> findAllByProjectId(Long id);
}
