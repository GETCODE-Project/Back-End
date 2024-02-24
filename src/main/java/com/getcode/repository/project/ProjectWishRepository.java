package com.getcode.repository.project;

import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import com.getcode.domain.project.WishProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectWishRepository extends JpaRepository<WishProject, Long> {
    WishProject findByProjectAndMember(Project project, Member member);

    List<WishProject> findByProject(Project project);
    Boolean existsByProjectIdAndMemberId(Long projectId, Long memberId);
}
