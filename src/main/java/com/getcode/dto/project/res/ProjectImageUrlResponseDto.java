package com.getcode.dto.project.res;

import com.getcode.domain.project.ProjectImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProjectImageUrlResponseDto {
    private Long id;
    private String imageUrl;

    public ProjectImageUrlResponseDto(ProjectImage projectImage){
        this.id = projectImage.getId();
        this.imageUrl = projectImage.getImageUrl();
    }

}
