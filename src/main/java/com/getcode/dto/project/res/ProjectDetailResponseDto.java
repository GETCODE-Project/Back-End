package com.getcode.dto.project.res;


import com.getcode.config.security.SecurityUtil;
import com.getcode.domain.project.*;
import com.getcode.dto.member.MemberInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private List<ProjectStackResponseDto> techStacks;
    private String subject;
    //private List<ProjectImageUrlResponseDto> imageUrls;
    private MemberInfoDto member;
    private boolean isWriter;
    private boolean checkWish;
    private boolean checkLike;
    private LocalDateTime createdDate, modifiedDate;


    public ProjectDetailResponseDto(Project project, Boolean checkLike, Boolean checkWish, Boolean checkWriter){

        this.projectId  = project.getId();
        this.title  = project.getTitle();
        this.content  = project.getContent();
        this.githubUrl  = project.getGithubUrl();
        this.views  = project.getViews();
        this.likeCnt  = project.getLikeCnt();
        this.techStacks  = project.getTechStacks().stream()
                                                    .map(ProjectStackResponseDto::new)
                                                    .collect(Collectors.toList());

        this.subject  = project.getSubject().print();
        /*
        this.imageUrls  = project.getProjectImages().stream()
                                                    .map(ProjectImageUrlResponseDto::new)
                                                    .collect(Collectors.toList());
        */
        this.member =MemberInfoDto.toDto(project.getMember());

        this.checkLike = checkLike;
        this.checkWish = checkWish;
        this.isWriter = checkWriter;
        this.createdDate = project.getCreateDate();
        this.modifiedDate = project.getModifiedDate();


    }





}
