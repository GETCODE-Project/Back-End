package com.getcode.domain.project;

import com.getcode.domain.common.BaseTimeEntity;
import com.getcode.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@DynamicInsert //insert시 null인 컬럼 제외
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(name ="github_url", nullable = true)
    private String githubUrl;

    //조회수 default값 설정
    @Column(columnDefinition = "integer default 0",nullable = false)
    private int views;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTech> techStacks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectImage> projectImages = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectSubject> projectSubjects = new ArrayList<>();


    public void stackAdd(ProjectTech projectTech){
        this.techStacks.add(projectTech);
    }
    public void projectSubjectAdd(ProjectSubject projectSubject){
        this.projectSubjects.add(projectSubject);
    }
    public void projectImageAdd (ProjectImage projectImage){
        this.projectImages.add(projectImage);
    }



}
