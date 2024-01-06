package com.getcode.dto.project.res;

import com.getcode.domain.common.Subject;
import com.getcode.domain.common.TechStack;
import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import com.getcode.domain.project.ProjectImage;
import com.getcode.domain.project.ProjectSubject;
import com.getcode.domain.project.ProjectTech;
import com.getcode.dto.member.MemberNickNameDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProjectDetailResponseDto {

    private Long project_id;
    private String title;
    private String content;
    private String githubUrl;
    private int views;
    private int likeCnt;
    private int wishCnt;
    private List<ProjectTech> techStackList;
    private List<ProjectSubject> projectSubjects;
    private List<ProjectImage> imageUrls;
    private MemberNickNameDto memberNickName;

    public ProjectDetailResponseDto toDto(Project project){
        return new ProjectDetailResponseDto(
        this.project_id  = project.getId(),
        this.title  = project.getTitle(),
        this.content  = project.getContent(),
        this.githubUrl  = project.getGithubUrl(),
        this.views  = project.getViews(),
        this.likeCnt  = project.getLikeCnt(),
        this.wishCnt  = project.getWishCnt(),
        this.techStackList  = project.getTechStacks(),
        this.projectSubjects  = project.getProjectSubjects(),
        this.imageUrls  = project.getProjectImages(),
       MemberNickNameDto.toDto(project.getMember())
       );
    }


}
