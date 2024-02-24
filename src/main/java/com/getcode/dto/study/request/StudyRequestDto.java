package com.getcode.dto.study.request;

import com.getcode.domain.common.Field;
import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;

import com.getcode.domain.study.StudyField;
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

    @Schema(description = "지역-시,도", defaultValue = "서울")
    private String siDo;

    @Schema(description = "지역-구,군", defaultValue = "중구")
    private String guGun;

    @Schema(description = "", defaultValue = "true")
    private boolean recruitment;

    @Schema(description = "온라인/오프라인", defaultValue = "true")
    private boolean online;

    @Schema(description = "연락방법", defaultValue = "[\"010-1234-5678\",\"ojs258@naver.com\"]")
    private List<String> contact;

    @Schema(description = "스터디 주제", defaultValue ="[\"Algorithm\",\"CodingTest\"]")
    private List<String> fields;

    public Study toEntity(Member member) {
        return Study.builder()
                .title(title)
                .content(content)
                .siDo(siDo)
                .guGun(guGun)
                .recruitment(true)
                .online(online)
                .views(0)
                .likeCnt(0)
                .contact(String.join("^", contact))
                .member(member)
                .build();
    }


}
