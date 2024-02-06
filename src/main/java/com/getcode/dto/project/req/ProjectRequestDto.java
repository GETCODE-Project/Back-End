package com.getcode.dto.project.req;


import com.getcode.domain.common.Subject;
import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
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



    private String title;
    private String content;
    private String introduction;
    private String githubUrl;
    private int views;
    private int likeCnt;
    //private List<String> imageUrls;
    private List<String> techStacks;
    private String subject;

    public Project toProjectEntity(Member member){


        return Project.builder()
                .member(member)
                .title(title)
                .content(content)
                .introduction(introduction)
                .githubUrl(githubUrl)
                .likeCnt(likeCnt)
                .views(views)
                .subject(Subject.fromString(subject))
                .build();
    }



}
