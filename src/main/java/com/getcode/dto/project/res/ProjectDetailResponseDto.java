package com.getcode.dto.project.res;


import com.getcode.domain.project.*;
import com.getcode.dto.member.MemberInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProjectDetailResponseDto {

    private Long projectId;
    private String title;
    private String content;
    private String githubUrl;
    private int views;
    private int likeCnt;
    private List<ProjectStackResponseDto> techStackList;
    private List<ProjectSubjectResponseDto> projectSubjects;
    private List<ProjectImageUrlResponseDto> imageUrls;
    private List<CommentResponseDto> comments;
    private MemberInfoDto memberInfo;

    public ProjectDetailResponseDto(Project project){

        this.projectId  = project.getId();
        this.title  = project.getTitle();
        this.content  = project.getContent();
        this.githubUrl  = project.getGithubUrl();
        this.views  = project.getViews();
        this.likeCnt  = project.getLikeCnt();
        this.techStackList  = project.getTechStacks().stream().map(ProjectStackResponseDto::new).collect(Collectors.toList());
        this.projectSubjects  = project.getProjectSubjects().stream().map(ProjectSubjectResponseDto::new).collect(Collectors.toList());
        this.imageUrls  = project.getProjectImages().stream().map(ProjectImageUrlResponseDto::new).collect(Collectors.toList());
        this.comments = project.getProjectComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
        MemberInfoDto.toDto(project.getMember());

    }




}