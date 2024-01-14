package com.getcode.dto.project.req;

import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import com.getcode.domain.project.ProjectComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentRequestDto {

    @Schema(description = "댓글 내용")
    private String content;

    public ProjectComment toEntity(Project project, Member member){
       return ProjectComment.builder()
                .content(content)
                .project(project)
                .member(member)
                .build();
    }

}
