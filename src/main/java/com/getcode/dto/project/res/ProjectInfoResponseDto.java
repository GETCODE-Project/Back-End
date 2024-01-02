package com.getcode.dto.project.res;

import com.getcode.domain.common.Subject;
import com.getcode.domain.common.TechStack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProjectInfoResponseDto {

    private Long project_id;
    private String title;
    private String content;
    private List<TechStack> techStackList;
    private List<Subject> subjects;


}
