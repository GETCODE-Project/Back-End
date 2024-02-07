package com.getcode.domain.common;

import java.util.List;
import java.util.stream.Stream;

public enum Field {
    CS("CS"),
    INTERVIEW("모의면접"),
    TOGETHER("모각코"),
    ALGORITHM("Algorithm"),
    CODINGTEST("CodingTest"),
    JAVA("Java"), C("C#") , PYTHON("Python"), PHP("php"), NODEJS("Node.js"),
    GO("Go"), RUBY("Ruby"), KOTLIN("Kotlin"), SWIFT("Swift"), PEAL("Peal"),

    SPRING("Spring"), DJANGO("Django"), EXPRESSJS("Express.js"), FLASK("Flask"), RAILS("Rails"),
    VEU("vue.js"), SPRINGBOOT("Springboot"), NEXT("Next.js"), NEST("Nest.js"),

    JAVASCRIPT("JavaScript"), TYPESCRIPT("TypeScript"), REACT("React"), REACTNATIVE("ReactNative"),
    HTML("Html"), CSS("Css"),

    FLUTTER("Flutter"), DART("Dart"),

    CI_CD("CI/CD"), LINUX("Linux"),
    GIT("Git"), GIT_HUB("GitHub"), GIT_FLOW("GitFlow"), GIT_HOOKS("GitHooks"),
    CLOUD_COMPUTING("CloudComputing"), DOCKER("Docker"),

    LICNESE("자격증"),
    EIP("정보처리기사"), OCJP("OCJP(국제)"),
    EIS("정보보안기사"), CISA("CISA(국제)"),CISSP("CISSP(국제)"),
    NETWORK_ADVISOR("네트워크관리사"), CCNA("CCNA(국제)"),
    LINUX_MASTER("리눅스마스터"),LPIC("LPIC(국제)"), MCSE("MCSE(국제)"),MCSA("MCSA(국제)"),
    ADSP("데이버분석준전문가(ADsP)"), ADP("데이터분석전문가(ADP)"),
    SQLD("SqlDeveloper"), OCP("OCP(국제)"),
    MS_AZURE("MSAzure(국제)"), AWSA("AWSArchitecture(국제)");


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
