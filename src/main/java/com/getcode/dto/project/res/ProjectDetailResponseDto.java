package com.getcode.dto.project.res;


import com.getcode.config.security.SecurityUtil;
import com.getcode.domain.member.Member;
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
    private LocalDateTime dateTime;
    private int views;
    private int likeCnt;
    private List<ProjectStackResponseDto> techStackList;
    private List<ProjectSubjectResponseDto> projectSubjects;
    private List<ProjectImageUrlResponseDto> imageUrls;
    private List<CommentResponseDto> comments;
    private String memberNickName;
    private boolean isWriter;
    private boolean checkWish;
    private boolean checkLike;



    public ProjectDetailResponseDto(Project project, ProjectLike projectLike, WishProject wishProject){

        this.projectId  = project.getId();
        this.title  = project.getTitle();
        this.content  = project.getContent();
        this.dateTime = project.getCreateDate();
        this.githubUrl  = project.getGithubUrl();
        this.views  = project.getViews();
        this.likeCnt  = project.getLikeCnt();
        this.techStackList  = project.getTechStacks().stream().map(ProjectStackResponseDto::new).collect(Collectors.toList());
        this.projectSubjects  = project.getProjectSubjects().stream().map(ProjectSubjectResponseDto::new).collect(Collectors.toList());
        this.imageUrls  = project.getProjectImages().stream().map(ProjectImageUrlResponseDto::new).collect(Collectors.toList());
        this.comments = project.getProjectComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
        this.memberNickName = project.getMember().getNickname();
        if (project.getMember().getEmail().equals(SecurityUtil.getCurrentMemberEmail())) {
            this.isWriter = true;
        } else {
            this.isWriter = false;
        }

        if(projectLike != null && projectLike.getMember().getEmail().equals(SecurityUtil.getCurrentMemberEmail())){
            this.checkLike = true;
        } else {
            this.checkLike = false;
        }

        if(wishProject != null && wishProject.getMember().getEmail().equals(SecurityUtil.getCurrentMemberEmail())){
            this.checkWish = true;
        } else {
            this.checkWish = false;
        }




    }




}
