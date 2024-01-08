package com.getcode.dto.study;

import com.getcode.domain.study.Study;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyTitleDto {
    String title;

    public static StudyTitleDto toDto(Study study) {
        return new StudyTitleDto(
                study.getTitle()
        );
    }
}
