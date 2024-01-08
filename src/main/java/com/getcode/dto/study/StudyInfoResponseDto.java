package com.getcode.dto.study;

import com.getcode.domain.study.Study;
import com.getcode.dto.member.MemberNicknameDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyInfoResponseDto {
    private String title;
    private String content;
    private String region;
    private boolean recruitment;
    private boolean online;
    private int views;
    private MemberNicknameDto member;
    private List<StudyCommentResponseDto> comments;

    public static StudyInfoResponseDto toDto(Study study) {
        return new StudyInfoResponseDto(
                study.getTitle(),
                study.getContent(),
                study.getRegion(),
                study.isRecruitment(),
                study.isOnline(),
                study.getViews(),
                MemberNicknameDto.toDto(study.getMember()),
                study.getComments().stream().map(StudyCommentResponseDto::toDto).collect(Collectors.toList())
        );
    }
}
