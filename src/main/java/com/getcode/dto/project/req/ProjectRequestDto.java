package com.getcode.dto.project.req;


import com.getcode.domain.common.Subject;
import com.getcode.domain.common.TechStack;
import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import com.getcode.domain.project.ProjectImage;
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

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectRequestDto {



    private String title;
    private String content;
    private String introduction;
    private String githubUrl;
    private int views;
    private int likeCnt;
    private int wishCnt;
    private List<String> imageUrls;
    private List<String> techStackList;
    private List<String> projectSubjects;


    public Project toProjectEntity(Member member){


        return Project.builder()
                .member(member)
                .title(title)
                .content(content)
                .introduction(introduction)
                .githubUrl(githubUrl)
                .likeCnt(likeCnt)
                .views(views)
                .build();
    }



}
