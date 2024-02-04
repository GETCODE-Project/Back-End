package com.getcode.repository.projectrecruitment;

import com.getcode.domain.member.Member;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRecruitmentLikeRepository extends JpaRepository<ProjectRecruitmentLike, Long> {
    ProjectRecruitmentLike findByProjectRecruitmentAndMember(ProjectRecruitment projectRecruitment, Member member);

    List<ProjectRecruitmentLike> findByProjectRecruitment(ProjectRecruitment projectRecruitment);

    Boolean existsByProjectRecruitmentIdAndMemberId(Long projectRecruitmentId, Long memberId);
}
