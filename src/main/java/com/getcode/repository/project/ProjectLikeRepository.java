package com.getcode.repository.project;

import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import com.getcode.domain.project.ProjectLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectLikeRepository extends JpaRepository<ProjectLike, Long> {
    ProjectLike findByProjectAndMember(Project project, Member member);

    List<ProjectLike> findByProject(Project project);

    Boolean existsByProjectIdAndMemberId(Long projectId, Long memberId);
}
