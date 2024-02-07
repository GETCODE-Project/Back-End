package com.getcode.domain.common;

import java.util.List;
import java.util.stream.Stream;

public enum Subject {

    TRAVLE("여행"),
    ECOMMERCE("이커머스"),
    SOCIAL("소셜네트워크"),
    SHARE("공유서비스"),
    MEDICAL("의료"),
    FINANCE("금융"),
    EDUCATION("교육"),
    MEETING("모임"),
    SPORT("스포츠"),
    GAME("게임"),
    REAL_ESTATE("부동산"),
    BEAUTY("뷰티"),
    FASION("패션");

    private String subject;

    Subject(String subject) {
        this.subject = subject;
    }

    public String print(){
        return subject;
    }

    public static Subject fromString(String reqValue) {
        for (Subject subject : Subject.values()) {
            if (subject.subject.equalsIgnoreCase(reqValue)) {
                return subject;
            }
        }
        throw new IllegalArgumentException();
    }

    public static List<String> subjectList() {
        return Stream.of(Subject.values())
                .map(Subject::print)
                .toList();
    }
}
