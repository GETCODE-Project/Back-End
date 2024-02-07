package com.getcode.dto.projectrecruitment.res;

import com.getcode.config.security.SecurityUtil;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentLike;
import com.getcode.domain.projectrecruitment.WishProjectRecruitment;
import com.getcode.dto.member.MemberInfoDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    private String subject;
    private List<ProjectRecruitmentStackResDto> techStacks;
    private boolean isWriter;
    private boolean checkLike;
    private boolean checkWish;
    private MemberInfoDto member;
    private List<String> contact;

    public ProjectRecruitmentDetailResDto(ProjectRecruitment projectRecruitment, Boolean checkLike, Boolean checkWish, Boolean checkWriter){
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
                    this.createDate = projectRecruitment.getCreateDate();
                    this.modifiedDate = projectRecruitment.getModifiedDate();
                    this.member =MemberInfoDto.toDto(projectRecruitment.getMember());
                    this.isWriter = checkWriter;
                    this.checkLike = checkLike;
                    this.checkWish = checkWish;
                    this.contact = Arrays.stream(projectRecruitment.getContact().split("\\^")).toList();
    }



}
