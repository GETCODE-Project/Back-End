package com.getcode.domain.project;

import com.getcode.domain.common.Subject;
import com.getcode.domain.common.TechStack;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProjectTech {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_tech_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tech_name")
    private TechStack techStack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public ProjectTech (String techStack){
        this.techStack = TechStack.fromString(techStack);
    }

    public ProjectTech getTechStack (TechStack techStacks, Project project){
        return ProjectTech.builder()
                .techStack(techStacks)
                .project(project)
                .build();
    }

}
