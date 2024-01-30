package com.getcode.domain.project;

import com.getcode.domain.member.Member;
import com.getcode.dto.project.req.CommentUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProjectComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public void updateComment(CommentUpdateRequestDto requestDto){
        this.content = requestDto.getContent();
    }

    public void foreignkey(Project project){
        this.project = project;
        project.getProjectComments().add(this);
    }

    public void foreignkey(Member member){
        this.member = member;
        member.getProjectComments().add(this);
    }

}
