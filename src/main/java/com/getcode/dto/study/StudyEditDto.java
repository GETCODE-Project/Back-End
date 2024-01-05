package com.getcode.dto.study;

import com.getcode.domain.study.Study;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyEditDto {
    private String title;
    private String content;
    private String region;
    private boolean recruitment;
    private boolean online;

    public static StudyEditDto toDto(Study study) {
        return new StudyEditDto(
                study.getTitle(),
                study.getContent(),
                study.getRegion(),
                study.isOnline(),
                study.isRecruitment()
        );
    }
}
