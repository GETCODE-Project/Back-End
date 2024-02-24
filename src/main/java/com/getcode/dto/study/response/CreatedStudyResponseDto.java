package com.getcode.dto.study.response;

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
    private Long id;
    private String title;
    private String content;
    private String siDo;
    private String guGun;
    private boolean recruitment;
    private boolean online;
    private int views;
    private int likeCnt;
    private String contact;
    private String date;
    private MemberInfoDto member;
    private List<String> fields;

    public static CreatedStudyResponseDto toDto(Study study, Member member, List<String> fields) {
        return new CreatedStudyResponseDto(
                study.getId(),
                study.getTitle(),
                study.getContent(),
                study.getSiDo(),
                study.getGuGun(),
                study.isRecruitment(),
                study.isOnline(),
                study.getViews(),
                study.getLikeCnt(),
                study.getContact(),
                study.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
                MemberInfoDto.toDto(member),
                fields
        );
    }

}
