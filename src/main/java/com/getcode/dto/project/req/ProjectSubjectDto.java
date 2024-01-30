package com.getcode.dto.project.req;

import com.getcode.domain.common.Subject;
import com.getcode.domain.project.Project;
import com.getcode.domain.project.ProjectSubject;

public class ProjectSubjectDto {

    public static ProjectSubject toEntity(Project project, String subject){
        return ProjectSubject.builder()
                .project(project)
                .subject(Subject.fromString(subject))
                .build();
    }

}
