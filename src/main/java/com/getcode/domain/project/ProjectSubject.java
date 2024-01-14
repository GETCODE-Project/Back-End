package com.getcode.domain.project;


import com.getcode.domain.common.Subject;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
public class ProjectSubject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_subject_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject_name")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;


    public ProjectSubject(String subject){
        this.subject = Subject.fromString(subject);
    }

    public ProjectSubject(String subject, Project project){
        this.subject = Subject.fromString(subject);
        this.project = project;
    }


    public ProjectSubject getSubject(Subject subject, Project project){
        return ProjectSubject.builder()
                .subject(subject)
                .project(project)
                .build();
    }


}
