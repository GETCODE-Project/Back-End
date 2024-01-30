package com.getcode.domain.project;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProjectImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_image_id")
    private Long id;

    @Column(name = "project_image_url", nullable = true)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public ProjectImage getImage(String imageUrl, Project project){
        return ProjectImage.builder()
                .imageUrl(imageUrl)
                .project(project)
                .build();
    }

    public ProjectImage(String imageUrl, Project project) {
        this.imageUrl = imageUrl;
        this.project = project;
    }

    public void foreignkey(Project project){
        this.project = project;
        project.getProjectImages().add(this);
    }


}