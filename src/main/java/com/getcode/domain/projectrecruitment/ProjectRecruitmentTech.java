package com.getcode.domain.projectrecruitment;

import com.getcode.domain.common.TechStack;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProjectRecruitmentTech {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_recruitment_tech_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tech_name")
    private TechStack techStack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_recruitment_id")
    private ProjectRecruitment projectRecruitment;

    public ProjectRecruitmentTech(ProjectRecruitment projectRecruitment, String techStack){
        this.techStack = TechStack.fromString(techStack);
        this.projectRecruitment = projectRecruitment;
    }

    public void foreignkey(ProjectRecruitment projectRecruitment){
        this.projectRecruitment = projectRecruitment;
        projectRecruitment.getTechStacks().add(this);
    }

}
