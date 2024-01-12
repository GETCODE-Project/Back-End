package com.getcode.domain.projectrecruitment;

import com.getcode.domain.common.BaseTimeEntity;
import com.getcode.domain.member.Member;


import com.getcode.domain.project.Project;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
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
    private String region;

    @Column(nullable = false)
    private boolean recruitment;

    @Column(nullable = false)
    private boolean online;

    @Column(nullable = false)
    private int views;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;




}
