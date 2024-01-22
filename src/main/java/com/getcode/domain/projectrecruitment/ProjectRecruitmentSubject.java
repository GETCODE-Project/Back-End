package com.getcode.domain.projectrecruitment;


import com.getcode.domain.common.Subject;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProjectRecruitmentSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_recruitment_subject_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject_name")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_recruitment_id")
    private ProjectRecruitment projectRecruitment;

    public void foreignkey(ProjectRecruitment projectRecruitment){
        this.projectRecruitment = projectRecruitment;
        projectRecruitment.getSubjects().add(this);
    }

}
