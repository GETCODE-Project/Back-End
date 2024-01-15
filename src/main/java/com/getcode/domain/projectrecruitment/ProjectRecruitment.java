package com.getcode.domain.projectrecruitment;

import com.getcode.domain.common.BaseTimeEntity;
import com.getcode.domain.member.Member;


import com.getcode.domain.project.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    //조회수 default 값 설정
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int views;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int likeCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "projectRecruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectRecruitmentTech> techStacks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "projectRecruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectRecruitmentSubject> Subjects = new ArrayList<>();

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


}
