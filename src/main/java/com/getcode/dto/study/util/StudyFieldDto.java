package com.getcode.dto.study.util;

import com.getcode.domain.common.Field;
import com.getcode.domain.study.Study;
import com.getcode.domain.study.StudyField;

public class StudyFieldDto {

    public static StudyField toEntity(Study study, String field) {
        return StudyField.builder()
                .field(Field.fromString(field))
                .study(study)
                .build();
    }
}
