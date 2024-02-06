package com.getcode.dto.projectrecruitment.res;

import com.getcode.config.security.SecurityUtil;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class RecruitmentCommentResDto {

    private Long id;
    private String content;
    private String memberNickName;
    private Boolean isWriter;
    private String modifiedDate;

    public RecruitmentCommentResDto(ProjectRecruitmentComment projectRecruitmentComment){
        this.id = projectRecruitmentComment.getId();
        this.content = projectRecruitmentComment.getContent();
        this.memberNickName = projectRecruitmentComment.getMember().getNickname();
        if(projectRecruitmentComment.getMember().getEmail().equals(SecurityUtil.getCurrentMemberEmail())){
            isWriter = true;
        }else {
            isWriter = false;
        }

    }


}
