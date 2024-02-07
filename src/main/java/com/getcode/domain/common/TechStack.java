package com.getcode.domain.common;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Stream;

public enum TechStack {

    //백엔드 언어
    JAVA("Java"), C("C#") , PYTHON("Python"), PHP("php"), NODEJS("Node.js"),
    GO("Go"), RUBY("Ruby"), KOTLIN("Kotlin"), SWIFT("Swift"), PEAL("Peal"),

    //프레임 워크
    SPRING("Spring"), DJANGO("Django"), EXPRESSJS("Express.js"), FLASK("Flask"), RAILS("Rails"),
    VEU("vue.js"), SPRINGBOOT("Springboot"), NEXT("Next.js"), NEST("Nest.js"),

    //데이터베이스
    MYSQL("MySQL"), ORACLE("Oracle"), POSTGRESQL("PostgreSQL"), MARIADB("MariaDB"),
    REDIS("Redis"), MONGODB("MongoDB"),

    //프론트 언어
    JAVASCRIPT("JavaScript"), TYPESCRIPT("TypeScript"), REACT("React"), REACTNATIVE("ReactNative"),
    HTML("Html"), CSS("Css"),

    //안드로이드
    FLUTTER("Flutter"), DART("Dart"),

    //기타
    GIT("Git"), GITHUB("Github"), AWS("AWS");

    private String stack;

    TechStack(String stack) {
        this.stack = stack;
    }

    public static TechStack fromString(String reqValue) {
        for (TechStack techStack : TechStack.values()) {
            if (techStack.stack.equalsIgnoreCase(reqValue)) {
                return techStack;
            }
        }
        throw new IllegalArgumentException();
    }

    public static List<String> techStackList() {
        return Stream.of(TechStack.values())
                .map(TechStack::print)
                .toList();
    }

    public String print(){
        return stack;
    }
}
