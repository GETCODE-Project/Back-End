package com.getcode.repository.projectrecruitment;

import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRecruitmentRepository extends JpaRepository<ProjectRecruitment, Long> {

    @Query("select p from ProjectRecruitment p where p.id in (select wp.projectRecruitment.id from WishProjectRecruitment wp where wp.member.id = :memberId)")
    List<ProjectRecruitment> findAllWishRecruitByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
