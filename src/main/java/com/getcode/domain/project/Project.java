package com.getcode.domain.project;

import com.getcode.domain.common.BaseTimeEntity;
import com.getcode.domain.common.Subject;
import com.getcode.domain.common.TechStack;
import com.getcode.domain.member.Member;
import com.getcode.dto.project.req.ProjectUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Column(nullable = false)
    private String introduction;

    @Column(name ="github_url", nullable = true)
    private String githubUrl;

    //조회수 default값 설정
    @Column(columnDefinition = "integer default 0",nullable = false)
    private int views;

    //좋아요수 default값 설정
    @Column(columnDefinition = "integer default 0",nullable = false)
    private int likeCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject_name")
    private Subject subject;


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
    private List<ProjectComment> projectComments = new ArrayList<>();



    public void updateProject(ProjectUpdateRequestDto requestDto){

            this.title = requestDto.getTitle();
            this.content = requestDto.getContent();
            this.introduction = requestDto.getIntroduction();
            this.githubUrl = requestDto.getGithubUrl();
            this.subject = Subject.fromString(requestDto.getSubject());

        //casecade 타입을 all로 설정해놓아서 기존 부모와 연결된 List객체를 삭제하고 새로 만들어준다.
        /*
        if(requestDto.getImageUrls() != null) {
            this.getProjectImages().clear();
            List<ProjectImage> newImage = requestDto.getImageUrls().stream()
                    .map(projectImage -> new ProjectImage(projectImage, this))
                    .collect(Collectors.toList());
            this.getProjectImages().addAll(newImage);
        }
*/
        if(requestDto.getTechStackList() != null) {
            this.getTechStacks().clear();
            List<ProjectTech> newStack = requestDto.getTechStackList().stream()
                    .map(projectStack -> new ProjectTech(projectStack, this))
                    .collect(Collectors.toList());
            this.techStacks.addAll(newStack);
        }


    }


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
        member.getProjects().add(this);
    }



}