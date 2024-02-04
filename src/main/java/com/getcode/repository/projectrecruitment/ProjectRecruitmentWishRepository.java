package com.getcode.repository.projectrecruitment;

import com.getcode.domain.member.Member;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.projectrecruitment.WishProjectRecruitment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRecruitmentWishRepository extends JpaRepository<WishProjectRecruitment, Long> {
    WishProjectRecruitment findByProjectRecruitmentAndMember(ProjectRecruitment projectRecruitment, Member member);

    List<WishProjectRecruitment> findByProjectRecruitment(ProjectRecruitment projectRecruitment);

    Boolean existsByProjectRecruitmentIdAndMemberId(Long projectRecruitmentId, Long memberId);
}
