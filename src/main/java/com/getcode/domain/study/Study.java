package com.getcode.domain.study;

import com.getcode.domain.common.BaseTimeEntity;
import com.getcode.domain.member.Member;
import com.getcode.dto.study.StudyEditDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
    private String region;

    @Column(nullable = false)
    private boolean recruitment;

    @Column(nullable = false)
    private boolean online;

    @Column(nullable = false)
    private int views;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private List<StudyComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private List<StudyLike> likes = new ArrayList<>();

    @Column(nullable = false)
    private int count;

    public void increaseViews() {
        this.views +=1;
    }

    public void increaseCount() {
        this.count +=1;
    }

    public void editStudy(StudyEditDto req) {
        this.title = req.getTitle();
        this.content = req.getContent();
        this.region = req.getRegion();
        this.online = req.isOnline();
        this.recruitment = req.isRecruitment();
    }
}
