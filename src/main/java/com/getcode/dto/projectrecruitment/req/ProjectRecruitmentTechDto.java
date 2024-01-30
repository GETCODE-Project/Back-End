package com.getcode.dto.projectrecruitment.req;

import com.getcode.domain.common.TechStack;
import com.getcode.domain.member.Member;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentTech;

public class ProjectRecruitmentTechDto {

    public static ProjectRecruitmentTech toEntity(ProjectRecruitment projectRecruitment, String stack){
        return ProjectRecruitmentTech.builder()
                .projectRecruitment(projectRecruitment)
                .techStack(TechStack.fromString(stack))
                .build();
    }

}
