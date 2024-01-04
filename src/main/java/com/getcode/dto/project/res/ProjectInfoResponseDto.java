package com.getcode.dto.project.res;

import com.getcode.domain.common.Subject;
import com.getcode.domain.common.TechStack;
import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import com.getcode.domain.project.ProjectImage;
import com.getcode.domain.project.ProjectSubject;
import com.getcode.domain.project.ProjectTech;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProjectInfoResponseDto {

    private Long project_id;
    private String title;
    private String content;
    private String githubUrl;
    private int views;
    private List<ProjectTech> techStackList;
    private List<ProjectSubject> projectSubjects;
    private List<ProjectImage> imageUrls;


    public Project fromProjectEntity(Member member){
        return Project.builder()
                .member(member)
                .title(title)
                .content(content)
                .githubUrl(githubUrl)
                .views(views)
                .projectImages(imageUrls)
                .projectSubjects(projectSubjects)
                .techStacks(techStackList)
                .build();
    }


}
