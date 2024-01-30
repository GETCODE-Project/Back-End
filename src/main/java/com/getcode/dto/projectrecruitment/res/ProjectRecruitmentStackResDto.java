package com.getcode.dto.projectrecruitment.res;

import com.getcode.domain.common.TechStack;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentTech;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProjectRecruitmentStackResDto {

    private Long id;
    private String teckStack;

    public ProjectRecruitmentStackResDto(ProjectRecruitmentTech projectRecruitmentTech){
        this.id = projectRecruitmentTech.getId();
        this.teckStack = String.valueOf(projectRecruitmentTech.getTechStack().print());
    }

}
