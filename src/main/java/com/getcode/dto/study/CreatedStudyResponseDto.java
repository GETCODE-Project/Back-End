package com.getcode.dto.study;

import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;
import com.getcode.dto.member.MemberInfoDto;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatedStudyResponseDto {
    private String title;
    private String content;
    private String region;
    private boolean recruitment;
    private boolean online;
    private int views;
    private int count;
    private String contact;
    private String date;
    private MemberInfoDto member;
    private List<String> subjects;

    public static CreatedStudyResponseDto toDto(Study study, Member member, List<String> subjects) {
        return new CreatedStudyResponseDto(
                study.getTitle(),
                study.getContent(),
                study.getRegion(),
                study.isRecruitment(),
                study.isOnline(),
                study.getViews(),
                study.getCount(),
                study.getContact(),
                study.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
                MemberInfoDto.toDto(member),
                subjects
        );
    }

}
