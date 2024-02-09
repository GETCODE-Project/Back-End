package com.getcode.dto.study.response;
;
import com.getcode.domain.study.StudyComment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyCommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private String email;
    private String nickname;
    private boolean isWriter;
    public static StudyCommentResponseDto toDto(StudyComment studyComment, boolean isWriter) {
        return new StudyCommentResponseDto (
                studyComment.getId(),
                studyComment.getContent(),
                studyComment.getCreateDate(),
                studyComment.getModifiedDate(),
                studyComment.getMember().getEmail(),
                studyComment.getMember().getNickname(),
                isWriter
        );
    }
}
