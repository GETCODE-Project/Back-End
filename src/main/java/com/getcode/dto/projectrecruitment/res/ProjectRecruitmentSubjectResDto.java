package com.getcode.dto.projectrecruitment.res;

import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentSubject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProjectRecruitmentSubjectResDto {

    private Long id;
    private String subject;

    public ProjectRecruitmentSubjectResDto(ProjectRecruitmentSubject projectRecruitmentSubject){
        this.id = projectRecruitmentSubject.getId();
        this.subject = String.valueOf(projectRecruitmentSubject.getSubject());
    }


}
