package com.getcode.dto.project.res;

import com.getcode.domain.project.ProjectComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentResponseDto {
    private Long id;
    private String memberNickName;
    @Schema(description = "댓글 내용")
    private String content;

    public CommentResponseDto(ProjectComment projectComment){

        this.id = projectComment.getId();
        this.content = projectComment.getContent();
        this.memberNickName = projectComment.getMember().getNickname();

    }

}