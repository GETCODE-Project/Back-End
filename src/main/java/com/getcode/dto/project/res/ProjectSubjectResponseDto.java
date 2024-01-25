package com.getcode.dto.project.res;

import com.getcode.domain.project.ProjectSubject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProjectSubjectResponseDto {

    private Long id;
    private String subject;

    public ProjectSubjectResponseDto(ProjectSubject subject){
        this.id = subject.getId();
        this.subject = String.valueOf(subject.getSubject().print());
    }

}
