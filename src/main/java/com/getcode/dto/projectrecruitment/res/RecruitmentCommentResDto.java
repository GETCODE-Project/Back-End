package com.getcode.dto.projectrecruitment.res;

import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentComment;

public class RecruitmentCommentResDto {

    private Long id;
    private String content;
    private String memberNickName;

    public RecruitmentCommentResDto(ProjectRecruitmentComment projectRecruitmentComment){
        this.id = projectRecruitmentComment.getId();
        this.content = projectRecruitmentComment.getContent();
        this.memberNickName = projectRecruitmentComment.getMember().getNickname();
    }


}
