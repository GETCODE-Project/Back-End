package com.getcode.domain.project;

import com.getcode.domain.common.BaseTimeEntity;
import com.getcode.domain.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Project extends BaseTimeEntity {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Size(min = 10) //게시물 작성시 최소 글자
    @Lob
    @Column(nullable = false)
    private String content;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(nullable = false)
        private int views;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")

    private Member member;





}
