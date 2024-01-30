package com.getcode.dto.project.req;


import com.getcode.domain.common.TechStack;
import com.getcode.domain.project.Project;

import com.getcode.domain.project.ProjectTech;

public class ProjectTechDto {

    public static ProjectTech toEntity(Project project, String techStack){
        return ProjectTech.builder()
                .project(project)
                .techStack(TechStack.fromString(techStack))
                .build();
    }
}
