package com.getcode.repository.project;

import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import com.getcode.domain.project.ProjectLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectLikeRepository extends JpaRepository<ProjectLike, Long> {
    ProjectLike findByProjectAndMember(Project project, Member member);

    ProjectLike findByProject(Project project);
}
