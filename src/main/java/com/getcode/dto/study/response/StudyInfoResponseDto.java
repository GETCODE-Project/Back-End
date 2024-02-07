package com.getcode.dto.study.response;

import com.getcode.domain.study.Study;
import com.getcode.dto.member.MemberInfoDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyInfoResponseDto {
    private Long id;
    private String title;
    private String content;
    private String siDo;
    private String guGun;
    private boolean recruitment;
    private boolean online;
    private int views;
    private int likeCnt;
    private List<String> contact;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private MemberInfoDto member;
    private List<StudyCommentResponseDto> comments;
    private List<String> studyFields;

    public static StudyInfoResponseDto toDto(Study study) {
        return new StudyInfoResponseDto(
                study.getId(),
                study.getTitle(),
                study.getContent().substring(0,15),
                study.getSiDo(),
                study.getGuGun(),
                study.isRecruitment(),
                study.isOnline(),
                study.getViews(),
                study.getLikeCnt(),
                Arrays.stream(study.getContact().split("\\^")).toList(),
                study.getCreateDate(),
                study.getModifiedDate(),
                MemberInfoDto.toDto(study.getMember()),
                study.getComments().stream().map(StudyCommentResponseDto::toDto).collect(Collectors.toList()),
                study.getFields().stream().map(sf -> sf.getField().print()).toList()
        );
    }

}
