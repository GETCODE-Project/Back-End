package com.getcode.domain.project;

import com.getcode.domain.common.BaseTimeEntity;
import com.getcode.domain.member.Member;
import com.getcode.dto.project.req.ProjectUpdateRequestDto;
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

    //좋아요수 default값 설정
    @Column(columnDefinition = "integer default 0",nullable = false)
    private int likeCnt;

    //조회수 default값 설정
    @Column(columnDefinition = "integer default 0",nullable = false)
    private int wishCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectLike> projectLikes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishProject> wishProjects = new ArrayList<>();

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

    public void updateProject(ProjectUpdateRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.githubUrl = requestDto.getGithubUrl();
        this.techStacks = requestDto.getTechStackList();
        this.projectImages = requestDto.getImageUrls();
        this.projectSubjects = requestDto.getProjectSubjects();
    }


    public void likeCntUp(){
        this.likeCnt += 1;
    }
    public void likeCntDown(){
        this.likeCnt -= 1;
    }
    public void wishCntUp(){
        this.wishCnt += 1;
    }
    public void wishCntDown(){
        this.wishCnt -= 1;
    }



}
