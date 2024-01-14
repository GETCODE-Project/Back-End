package com.getcode.dto.study;

import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;

import com.getcode.domain.study.StudySubject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyRequestDto {

    @Schema(description = "제목", defaultValue = "Java 스터디")
    @NotBlank(message = "제목은 필수입니다.")
    @Length(min = 2, max = 15)
    private String title;

    @Schema(description = "내용", defaultValue = "Java 스터디 모집합니다.")
    @NotBlank(message = "내용은 필수입니다.")
    @Length(min = 2, max = 1000)
    private String content;

    @Schema(description = "지역", defaultValue = "서울")
    private String region;

    @Schema(description = "온라인/오프라인", defaultValue = "true")
    private boolean online;

    @Schema(description = "연락방법", defaultValue = "kyun9151@naver.com")
    private String contact;

    @Schema(description = "스터디 주제", defaultValue = "코딩 테스트, 자격증")
    private List<String> subjects;

    public Study toEntity(Member member) {
        return Study.builder()
                .title(title)
                .content(content)
                .region(region)
                .recruitment(true)
                .online(online)
                .views(0)
                .count(0)
                .contact(contact)
                .member(member)
                .build();
    }


}
