package com.getcode.repository.projectrecruitment;

import com.getcode.domain.projectrecruitment.ProjectRecruitmentComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRecruitmentCommentRepository extends JpaRepository<ProjectRecruitmentComment, Long> {


    List<ProjectRecruitmentComment> findByProjectRecruitmentId(Long recruitmentId);
}
