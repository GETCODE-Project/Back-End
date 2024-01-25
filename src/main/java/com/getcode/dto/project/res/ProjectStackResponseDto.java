package com.getcode.dto.project.res;

import com.getcode.domain.project.ProjectTech;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProjectStackResponseDto {
    private Long id;
    private String techStack;

    public ProjectStackResponseDto(ProjectTech projectTech){
        this.id = projectTech.getId();
        this.techStack = String.valueOf(projectTech.getTechStack().print());
    }

}
