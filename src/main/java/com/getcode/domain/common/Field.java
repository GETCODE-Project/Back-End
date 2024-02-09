package com.getcode.domain.common;

import java.util.List;
import java.util.stream.Stream;

public enum Field {
    CS("CS"),
    INTERVIEW("모의면접"),
    TOGETHER("모각코"),
    ALGORITHM("Algorithm"),
    CODINGTEST("CodingTest"),
    PROGRAMING_LANGUAGE("ProgramingLanguage"),
    BACK_END("BackEnd"),
    FRONT_END("FrontEnd"),
    MOBILE("mobile"),
    CI_CD("CI/CD"),
    LINUX("Linux"),
    LICNESE("자격증");


    private String studyField;

    Field(String subject) {
        this.studyField = subject;
    }

    public static Field fromString(String reqValue) {
        for (Field field : Field.values()) {
            if (field.studyField.equalsIgnoreCase(reqValue)) {
                return field;
            }
        }
        throw new IllegalArgumentException();
    }

    public static List<String> studyFieldList() {
        return Stream.of(Field.values())
                .map(Field::print)
                .toList();
    }

    public String print(){
        return studyField;
    }
}
