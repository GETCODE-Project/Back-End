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
    private String memberNickName;
    private List<String> studyFields;
    private boolean checkLike;
    private boolean checkWish;

    public static StudyInfoResponseDto toDto(Study study, boolean checkLike, boolean checkWish) {
        return new StudyInfoResponseDto(
                study.getId(),
                study.getTitle(),
                study.getContent(),
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
                study.getFields().stream().map(sf -> sf.getField().print()).toList(),
                checkLike,
                checkWish
        );
    }

/*
    public  StudyInfoResponseDto (Study study, boolean checkLike, boolean checkWish) {
                this.id = study.getId(),
                this.title = study.getTitle(),
                this.content = study.getContent(),
                this.siDo = study.getSiDo(),
                this.guGun = study.getGuGun(),
                this.recruitment = study.isRecruitment(),
                this.online = study.isOnline(),
                this.views = study.getViews(),
                this.likeCnt = study.getLikeCnt(),
                this.contact = Arrays.stream(study.getContact().split("\\^")).toList(),
                this.createDate = study.getCreateDate(),
                this.modifiedDate = study.getModifiedDate(),
                this.memberNickName = study.getMember().getNickname(),
                this.studyFields = study.getFields().stream().map(sf -> sf.getField().print()).toList(),
                this.checkLike = checkLike,
                this.checkWish = checkWish;
    }
*/
}
