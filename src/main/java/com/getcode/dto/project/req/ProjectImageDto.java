package com.getcode.dto.project.req;

import com.getcode.domain.project.Project;
import com.getcode.domain.project.ProjectImage;

public class ProjectImageDto {

    public static ProjectImage toEntity(Project project, String url){
        return ProjectImage.builder()
                .project(project)
                .imageUrl(url)
                .build();
    }
}
