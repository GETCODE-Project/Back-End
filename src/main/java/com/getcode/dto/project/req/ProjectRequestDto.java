package com.getcode.dto.project.req;


import com.getcode.domain.common.Subject;
import com.getcode.domain.common.TechStack;
import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import com.getcode.domain.project.ProjectSubject;
import com.getcode.domain.project.ProjectTech;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectRequestDto {


    @Schema(description = "제목", defaultValue = "GETCODE")
    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @Schema(description = "내용", defaultValue = "프로젝트를 공유하고 팀원 모집과 스터디 모집을 할 수 있는 사이트입니다.")
    @NotBlank(message = "내용을 입력해주세요")
    @Size(min = 10, message = "내용은 최소 10글자 이상이어야 합니다.")
    private String content;

    @Schema(description = "깃헙주소", defaultValue = "https://github.com/GETCODE-Project")
    @Pattern(regexp = "^https:\\/\\/github\\.com\\/+[A-Za-z0-9-]+$", message = "GitHub URL 형식에 맞지 않습니다.")
    @NotBlank(message = "깃허브 주소를 입력해주세요")
    private String githubUrl;

    @Schema(description = "조회수")
    private int views;

    @Schema(description = "이미지 url")
    private String imageUrl;

    @Schema(description = "기술스택")
    private List<TechStack> techStackList;

    @Schema(description = "프로젝트 주제")
    private Subject subject;


    public Project toEntity(Member member){
        return Project.builder()
                .member(member)
                .title(title)
                .content(content)
                .githubUrl(githubUrl)
                .views(views)
                .imageUrl(imageUrl)
                .build();
    }

    public ProjectTech toEntity(Project project){
        return ProjectTech.builder()
                .project(project)
                .techStackList(techStackList)
                .build();
    }
    

    public ProjectSubject Entity(Project project){
        return ProjectSubject.builder()
                .project(project)
                .subject(subject)
                .build();
    }
    
    
    







}
