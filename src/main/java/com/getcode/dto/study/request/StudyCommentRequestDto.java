package com.getcode.dto.study.request;

import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;
import com.getcode.domain.study.StudyComment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyCommentRequestDto {
    private String content;
    public StudyComment toEntity(Study study, Member member) {
        return StudyComment.builder()
                .content(content)
                .member(member)
                .study(study)
                .build();
    }
}
