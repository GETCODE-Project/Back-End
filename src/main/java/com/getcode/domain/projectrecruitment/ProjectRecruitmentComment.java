package com.getcode.domain.projectrecruitment;


import com.getcode.domain.member.Member;
import com.getcode.dto.projectrecruitment.req.RecruitmentCommentUpdateDto;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProjectRecruitmentComment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_recruitment_comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_recruitment_id")
    private ProjectRecruitment projectRecruitment;


    public void update(RecruitmentCommentUpdateDto dto){
        this.content = dto.getContent();
    }

    public void foreignkey(ProjectRecruitment projectRecruitment){
        this.projectRecruitment = projectRecruitment;
        projectRecruitment.getComments().add(this);
    }

    public void foreignkey(Member member){
        this.member = member;
        member.getProjectRecruitmentComments().add(this);
    }



}
