package com.getcode.dto.projectrecruitment.res;

import com.getcode.config.security.SecurityUtil;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentLike;
import com.getcode.domain.projectrecruitment.WishProjectRecruitment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectRecruitmentDetailResDto {

    private String title;
    private String content;
    private String siDo;
    private String guGun;
    private boolean online;
    private boolean recruitment;
    private int views;
    private int likeCnt;
    private LocalDateTime createDate, modifiedDate;
    private List<ProjectRecruitmentSubjectResDto> subjects;
    private List<ProjectRecruitmentStackResDto> techStacks;
    private List<RecruitmentCommentResDto> comments;
    private boolean isWriter;
    private boolean checkLike;
    private boolean checkWish;


    public ProjectRecruitmentDetailResDto(ProjectRecruitment projectRecruitment, ProjectRecruitmentLike projectRecruitmentLike, WishProjectRecruitment wishProjectRecruitment){
                    this.title = projectRecruitment.getTitle();
                    this.content = projectRecruitment.getContent();
                    this.siDo = projectRecruitment.getSiDo();
                    this.guGun = projectRecruitment.getGuGun();
                    this.online = projectRecruitment.isOnline();
                    this.recruitment = projectRecruitment.isRecruitment();
                    this.views = projectRecruitment.getViews();
                    this.likeCnt = projectRecruitment.getLikeCnt();
                    this.techStacks = projectRecruitment.getTechStacks().stream().map(ProjectRecruitmentStackResDto::new).collect(Collectors.toList());
                    this.subjects = projectRecruitment.getSubjects().stream().map(ProjectRecruitmentSubjectResDto::new).collect(Collectors.toList());
                    this.comments = projectRecruitment.getComments().stream().map(RecruitmentCommentResDto::new).collect(Collectors.toList());
                    this.createDate = projectRecruitment.getCreateDate();
                    this.modifiedDate = projectRecruitment.getModifiedDate();
        if (projectRecruitment.getMember().getEmail().equals(SecurityUtil.getCurrentMemberEmail())) {
            this.isWriter = true;
        } else {
            this.isWriter = false;
        }

        if(projectRecruitmentLike != null && projectRecruitmentLike.getMember().getEmail().equals(SecurityUtil.getCurrentMemberEmail())){
            this.checkLike = true;
        } else {
            this.checkLike = false;
        }

        if(wishProjectRecruitment != null && wishProjectRecruitment.getMember().getEmail().equals(SecurityUtil.getCurrentMemberEmail())){
            this.checkWish = true;
        } else {
            this.checkWish = false;
        }


    }



}
