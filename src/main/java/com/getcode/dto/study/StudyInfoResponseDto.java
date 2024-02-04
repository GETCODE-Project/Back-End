package com.getcode.dto.study;

import com.getcode.domain.study.Study;
import com.getcode.domain.study.StudySubject;
import com.getcode.dto.member.MemberInfoDto;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
    private int count;
    private List<String> contact;
    private String date;
    private MemberInfoDto member;
    private List<StudyCommentResponseDto> comments;
    private List<String> subjects;

    public static StudyInfoResponseDto toDto(Study study) {
        return new StudyInfoResponseDto(
                study.getTitle(),
                study.getContent(),
                study.getRegion(),
                study.isRecruitment(),
                study.isOnline(),
                study.getViews(),
                study.getCount(),
                Arrays.stream(study.getContact().split("\\^")).toList(),
                study.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
                MemberInfoDto.toDto(study.getMember()),
                study.getComments().stream().map(StudyCommentResponseDto::toDto).collect(Collectors.toList()),
                study.getSubjects().stream().map(StudySubject::getSubject).collect(Collectors.toList())
        );
    }

}
