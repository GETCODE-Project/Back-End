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
    private String memberNickName;



    public ProjectRecruitmentInfoResDto(ProjectRecruitment projectRecruitment){
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
        this.modifiedDate = projectRecruitment.getModifiedDate();
        this.createDate = projectRecruitment.getCreateDate();
        this.memberNickName = projectRecruitment.getMember().getNickname();

    }

}
