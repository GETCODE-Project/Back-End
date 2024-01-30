package com.getcode.dto.project.req;

import com.getcode.domain.project.ProjectComment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentUpdateRequestDto {
    private String content;

    public CommentRequestDto toDto(ProjectComment projectComment){
        return new CommentRequestDto(
                projectComment.getContent()
        );
    }

}
