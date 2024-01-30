package com.getcode.dto.community;

import com.getcode.domain.community.Community;
import com.getcode.domain.community.CommunityCategory;
import com.getcode.dto.member.MemberInfoDto;
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
public class CommunityResponseDto {

    private String title;
    private String content;
    private int views;
    private int count;
    private String date;
    private CommunityCategory category;
    private MemberInfoDto member;
    private List<CommunityCommentResponseDto> comments;

    public static CommunityResponseDto toDto(Community community) {
        return new CommunityResponseDto(
                community.getTitle(),
                community.getContent(),
                community.getViews(),
                community.getCount(),
                community.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
                community.getCategory(),
                MemberInfoDto.toDto(community.getMember()),
                community.getComments().stream().map(CommunityCommentResponseDto::toDto).collect(Collectors.toList())
        );
    }
}
