package com.getcode.dto.projectrecruitment.req;

import com.getcode.domain.common.Subject;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentSubject;

public class ProjectRecruitmentSubjectDto {

    public static ProjectRecruitmentSubject toEntity(ProjectRecruitment projectRecruitment, String subject){
        return ProjectRecruitmentSubject.builder()
                .projectRecruitment(projectRecruitment)
                .subject(Subject.fromString(subject))
                .build();
    }

}
