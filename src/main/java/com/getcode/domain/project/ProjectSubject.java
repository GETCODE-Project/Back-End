package com.getcode.domain.project;


import com.getcode.domain.common.Subject;
import jakarta.persistence.*;
import lombok.*;


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

}
