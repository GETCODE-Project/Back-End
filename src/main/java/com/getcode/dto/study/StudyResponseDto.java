package com.getcode.dto.study;

import com.getcode.domain.study.Study;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyResponseDto {
    private String title;
    private String content;
    private String region;
    private boolean recruitment;
    private boolean online;
    private int views;
    private int count;
    private String contact;
    private String subject;

    public static StudyResponseDto toDto(Study study) {
        return new StudyResponseDto(
                study.getTitle(),
                study.getContent(),
                study.getRegion(),
                study.isRecruitment(),
                study.isOnline(),
                study.getViews(),
                study.getCount(),
                study.getContact(),
                study.getSubject()
        );
    }

}
