package com.getcode.dto.study;

import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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

    public Study toEntity(Member member) {
        return Study.builder()
                .title(title)
                .content(content)
                .region(region)
                .recruitment(true)
                .online(online)
                .views(0)
                .member(member)
                .build();
    }

}
