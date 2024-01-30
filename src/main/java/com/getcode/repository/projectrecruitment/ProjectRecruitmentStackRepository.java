package com.getcode.repository.projectrecruitment;

import com.getcode.domain.projectrecruitment.ProjectRecruitmentTech;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRecruitmentStackRepository extends JpaRepository<ProjectRecruitmentTech, Long> {
}
