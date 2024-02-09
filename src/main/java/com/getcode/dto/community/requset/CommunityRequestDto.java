package com.getcode.dto.community.requset;

import com.getcode.domain.community.Community;
import com.getcode.domain.common.CommunityCategory;
import com.getcode.domain.member.Member;
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
public class CommunityRequestDto {

    @Schema(description = "제목", defaultValue = "QueryDsl 질문")
    @NotBlank(message = "제목은 필수입니다.")
    @Length(min = 2, max = 15)
    private String title;

    @Schema(description = "내용", defaultValue = "QueryDsl 어떻게 설정하나요?")
    @NotBlank(message = "내용은 필수입니다.")
    @Length(min = 2, max = 1000)
    private String content;

    @Schema(description = "게시판 종류", defaultValue = "qna")
    private String category;

    public Community toEntity(Member member) {
        return Community.builder()
                .title(title)
                .content(content)
                .views(0)
                .likeCnt(0)
                .member(member)
                .category(CommunityCategory.valueOf(category.toUpperCase()))
                .build();
    }
}
