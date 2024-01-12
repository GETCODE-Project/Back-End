package com.getcode.dto.project.req;

import com.getcode.domain.project.Project;
import com.getcode.domain.project.ProjectImage;
import com.getcode.domain.project.ProjectSubject;
import com.getcode.domain.project.ProjectTech;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateRequestDto {

    private String title;

    private String content;

    private String introduction;

    private String githubUrl;

    private List<ProjectImage> imageUrls;

    private List<String> techStackList;

    private List<String> projectSubjects;

    /*
    public static ProjectUpdateRequestDto toDto(Project project){
        return new ProjectUpdateRequestDto(
                project.getTitle(),
                project.getContent(),
                project.getIntroduction(),
                project.getGithubUrl(),
                project.getProjectImages(),
                project.getTechStacks(),
                project.getProjectSubjects()
        );
    }
*/


}
