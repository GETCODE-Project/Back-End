package com.getcode.dto.projectrecruitment.req;

import com.getcode.domain.member.Member;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentComment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentCommentRequestDto {

    private String content;

    public ProjectRecruitmentComment toEntity(ProjectRecruitment projectRecruitment, Member member){
        return  ProjectRecruitmentComment.builder()
                .projectRecruitment(projectRecruitment)
                .member(member)
                .content(content)
                .build();

    }

}
