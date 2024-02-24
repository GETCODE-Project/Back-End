package com.getcode.dto.projectrecruitment.res;


import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ProjectRecruitmentInfoResDto {

    private Long projectRecruitmentId;
    private String title;
    private String content;
    private String siDo;
    private String guGun;
    private boolean online;
    private boolean recruitment;
    private int views;
    private int likeCnt;
    private LocalDateTime createDate, modifiedDate;
    private String subject;
    private List<ProjectRecruitmentStackResDto> techStacks;
    private String memberNickName;
    private Boolean checkLike;
    private Boolean checkWish;



    public ProjectRecruitmentInfoResDto(ProjectRecruitment projectRecruitment){
        this.projectRecruitmentId = projectRecruitment.getId();
        this.title = projectRecruitment.getTitle();
        this.content = projectRecruitment.getContent();
        this.siDo = projectRecruitment.getSiDo();
        this.guGun = projectRecruitment.getGuGun();
        this.online = projectRecruitment.isOnline();
        this.recruitment = projectRecruitment.isRecruitment();
        this.views = projectRecruitment.getViews();
        this.likeCnt = projectRecruitment.getLikeCnt();
        this.techStacks = projectRecruitment.getTechStacks().stream().map(ProjectRecruitmentStackResDto::new).collect(Collectors.toList());
        this.subject = projectRecruitment.getSubject().print();
        this.modifiedDate = projectRecruitment.getModifiedDate();
        this.createDate = projectRecruitment.getCreateDate();
        this.memberNickName = projectRecruitment.getMember().getNickname();

    }

    public void setCheckLike(Boolean recruitmentLikedByUser) {
        this.checkLike = recruitmentLikedByUser;
    }

    public void setCheckWish(Boolean recruitmentWishedByUser) {
        this.checkWish = recruitmentWishedByUser;
    }
}
