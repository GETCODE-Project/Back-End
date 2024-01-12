package com.getcode.dto.study;

import com.getcode.domain.study.Study;
import com.getcode.domain.study.StudySubject;

public class StudySubjectDto {

    public static StudySubject toEntity(Study study, String subject) {
        return StudySubject.builder()
                .subject(subject)
                .study(study)
                .build();
    }
}
