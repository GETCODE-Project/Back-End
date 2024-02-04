package com.getcode.repository.project;


import com.getcode.domain.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
     boolean existsByGithubUrl(String githubUrl);

     Page<Project> findAll(Specification<Project> combinedSpec, Pageable pageable);

     @Query("select p from Project p where p.id in (select wp.project.id from WishProject wp where wp.member.id = :memberId)")
     List<Project> findAllWishProjectByMemberId(@Param("memberId") Long memberId, Pageable pageable);

     List<Project> findAllByMemberId(Long memberId, Pageable pageable);
}
