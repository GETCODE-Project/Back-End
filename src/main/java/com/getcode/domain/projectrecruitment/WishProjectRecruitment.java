package com.getcode.domain.projectrecruitment;

import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class WishProjectRecruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_project_recruitment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_recruitment_id")
    private ProjectRecruitment projectRecruitment;

    public void foreignkey(Member member){
        this.member = member;
        member.getWishProjectRecruitments().add(this);
    }

}
