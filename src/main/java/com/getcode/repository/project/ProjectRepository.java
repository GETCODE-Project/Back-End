package com.getcode.repository.project;


import com.getcode.domain.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
     boolean existsByGithubUrl(String githubUrl);
     Page<Project> findAll(Specification<Project> combinedSpec, Pageable pageable);


}
