package com.getcode.dto.community;

import com.getcode.domain.community.Community;
import com.getcode.domain.community.CommunityCategory;
import com.getcode.dto.member.MemberInfoDto;
import com.getcode.dto.study.StudyCommentResponseDto;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatedCommunityResponseDto {
    private String title;
    private String content;
    private int views;
    private int count;
    private String date;
    private CommunityCategory category;
    private MemberInfoDto member;

    public static CreatedCommunityResponseDto toDto(Community community) {
        return new CreatedCommunityResponseDto(
                community.getTitle(),
                community.getContent(),
                community.getViews(),
                community.getCount(),
                community.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
                community.getCategory(),
                MemberInfoDto.toDto(community.getMember())
        );
    }
}
