package com.getcode.dto.study;
;
import com.getcode.domain.study.StudyComment;
import java.time.format.DateTimeFormatter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyCommentResponseDto {
    private String content;
    private String modifiedDate;
    private String email;
    private String nickname;
    public static StudyCommentResponseDto toDto(StudyComment studyComment) {
        return new StudyCommentResponseDto (
                studyComment.getContent(),
                studyComment.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
                studyComment.getMember().getEmail(),
                studyComment.getMember().getNickname()
        );
    }
}
