package com.getcode.domain.study;

import com.getcode.domain.common.BaseTimeEntity;
import com.getcode.domain.common.Field;
import com.getcode.domain.member.Member;
import com.getcode.dto.study.request.StudyRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@AllArgsConstructor
public class Study extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String siDo;

    @Column(nullable = false)
    private String guGun;

    @Column(nullable = false)
    private boolean recruitment;

    @Column(nullable = false)
    private boolean online;

    @Column(columnDefinition = "integer default 0",nullable = false)
    private int views;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int likeCnt;

    private String contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private List<StudyComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private List<StudyLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private List<StudyField> fields = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private List<WishStudy> wishes = new ArrayList<>();

    public void increaseViews() {
        this.views +=1;
    }

    public void increaseCount() {
        this.likeCnt +=1;
    }

    public void decreaseCount() {
        this.likeCnt -=1;
    }

    public void editStudy(StudyRequestDto req) {
        this.title = req.getTitle();
        this.content = req.getContent();
        this.siDo = req.getSiDo();
        this.guGun = req.getGuGun();
        this.online = req.isOnline();
        this.recruitment = req.isRecruitment();
        List<StudyField> oldFields = this.fields;
        oldFields.clear();
    }
}
