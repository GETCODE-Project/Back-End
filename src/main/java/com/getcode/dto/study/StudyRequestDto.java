package com.getcode.dto.study;

import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;
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
    @NotBlank(message = "제목은 필수입니다.")
    @Length(min = 2, max = 15)
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    @Length(min = 2, max = 1000)
    private String content;

    private String region;
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
