package com.getcode.domain.projectrecruitment;

import com.getcode.domain.common.BaseTimeEntity;
import com.getcode.domain.common.Subject;
import com.getcode.domain.member.Member;


import com.getcode.domain.project.*;
import com.getcode.dto.projectrecruitment.req.RecruitmentUpdateRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProjectRecruitment extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_recruitment_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    @Size(min = 10)
    private String content;

    @Column(nullable = false)
    private String siDo;

    @Column(nullable = false)
    private String guGun;

    @Column(nullable = false)
    private boolean recruitment;

    @Column(nullable = false)
    private boolean online;

    @Column(nullable = false)
    private String contact;

    //조회수 default 값 설정
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int views;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int likeCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject_name")
    private Subject subject;

    @Builder.Default
    @OneToMany(mappedBy = "projectRecruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectRecruitmentTech> techStacks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "projectRecruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectRecruitmentLike> likes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "projectRecruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectRecruitmentComment> comments = new ArrayList<>();

    public void likeCntUp(){
        this.likeCnt += 1;
    }
    public void likeCntDown(){
        this.likeCnt -= 1;
    }
    public void viewCntUp(){
        this.views += 1;
    }

    public void foreignkey(Member member){
        this.member = member;
        member.getProjectRecruitments().add(this);
    }


    public void update(RecruitmentUpdateRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.siDo = requestDto.getSiDo();
        this.guGun = requestDto.getGuGun();
        this.online = requestDto.getOnline();
        this.recruitment = requestDto.getRecruitment();
        this.subject = Subject.fromString(requestDto.getSubject());
        //casecade 타입을 all로 설정해놓아서 기존 부모와 연결된 List객체를 삭제하고 새로 만들어준다.
        if(requestDto.getTechStackList() != null) {
            this.getTechStacks().clear();
            List<ProjectRecruitmentTech> newStack = requestDto.getTechStackList().stream()
                    .map(recruitmentTech -> new ProjectRecruitmentTech(this, recruitmentTech))
                    .collect(Collectors.toList());
            this.techStacks.addAll(newStack);
        }


    }
}