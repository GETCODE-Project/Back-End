package com.getcode.repository.projectrecruitment;

import com.getcode.domain.member.Member;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRecruitmentLikeRepository extends JpaRepository<ProjectRecruitmentLike, Long> {
    ProjectRecruitmentLike findByProjectRecruitmentAndMember(ProjectRecruitment projectRecruitment, Member member);

    ProjectRecruitmentLike findByProjectRecruitment(ProjectRecruitment projectRecruitment);

    Boolean existsByProjectRecruitmentIdAndMemberId(Long projectRecruitmentId, Long memberId);
}
