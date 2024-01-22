package com.getcode.repository.projectrecruitment;

import com.getcode.domain.member.Member;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.projectrecruitment.WishProjectRecruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRecruitmentWishRepository extends JpaRepository<WishProjectRecruitment, Long> {
    WishProjectRecruitment findByProjectRecruitmentAndMember(ProjectRecruitment projectRecruitment, Member member);

    WishProjectRecruitment findByProjectRecruitment(ProjectRecruitment projectRecruitment);
}
