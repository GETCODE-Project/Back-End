package com.getcode.domain.projectrecruitment;

import com.getcode.domain.common.TechStack;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}
