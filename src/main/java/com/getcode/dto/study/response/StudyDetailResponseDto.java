package com.getcode.dto.study.response;

import com.getcode.domain.study.Study;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class StudyDetailResponseDto {
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
    private String memberNickName;
    private List<StudyCommentResponseDto> comments;
    private List<String> studyFields;
    private boolean checkLike;
    private boolean checkWish;
    private boolean isWriter;

    public static StudyDetailResponseDto toDto(Study study, List<StudyCommentResponseDto> studyCommentResponseDtos,
                                               boolean checkLike, boolean checkWish,boolean isWriter) {
        return new StudyDetailResponseDto(
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
                study.getMember().getNickname(),
                studyCommentResponseDtos,
                study.getFields().stream().map(sf -> sf.getField().print()).toList(),
                checkLike,
                checkWish,
                isWriter
        );
    }
}
