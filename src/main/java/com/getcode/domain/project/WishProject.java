package com.getcode.domain.project;

import com.getcode.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class WishProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_project_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public void foreignkey(Member member){
        this.member = member;
        member.getWishProject().add(this);
    }

    public void foreignkey(Project project){
        this.project = project;
        project.getWishProjects().add(this);
    }
}
