package com.getcode.domain.project;

import jakarta.persistence.*;

@Entity
public class ProjectSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_subject_id")
    private Long id;

    @Column(name = "subject_name")
    private String subjectName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;


}
