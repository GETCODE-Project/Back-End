package com.getcode.dto.study.response;

import com.getcode.domain.study.Study;
import com.getcode.dto.member.MemberInfoDto;
import com.getcode.dto.projectrecruitment.res.ProjectRecruitmentStackResDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class StudyDetailResponseDto {
    private Long id;
    private String title;
    private String content;
    private String siDo;
    private String guGun;
    private boolean recruitment;
    private boolean online;
    private int views;
    private int likeCnt;
    private List<String> contact;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private MemberInfoDto member;
    private List<String> studyFields;
    private boolean checkLike;
    private boolean checkWish;
    private boolean isWriter;



    public StudyDetailResponseDto(Study study, Boolean checkLike, Boolean checkWish, Boolean isWriter){
        this.id = study.getId();
        this.title = study.getTitle();
        this.content = study.getContent();
        this.siDo = study.getSiDo();
        this.guGun = study.getGuGun();
        this.online = study.isOnline();
        this.recruitment = study.isRecruitment();
        this.views = study.getViews();
        this.likeCnt = study.getLikeCnt();
        this.studyFields = study.getFields().stream().map(sf -> sf.getField().print()).toList();
        this.createDate = study.getCreateDate();
        this.modifiedDate = study.getModifiedDate();
        this.member = MemberInfoDto.toDto(study.getMember());
        this.isWriter = isWriter;
        this.checkLike = checkLike;
        this.checkWish = checkWish;
        this.contact = Arrays.stream(study.getContact().split("\\^")).toList();
    }

}
