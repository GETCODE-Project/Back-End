package com.getcode.dto.community.response;

import com.getcode.domain.community.Community;

import java.time.LocalDateTime;
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
public class CommunityDetailResponseDto {
    private String title;
    private String content;
    private int views;
    private int count;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private String category;
    private String memberNickname;
    private boolean checkLike;
    private boolean checkWish;
    private boolean isWriter;
    private List<CommunityCommentResponseDto> comments;


    public static CommunityDetailResponseDto toDto(Community community, List<CommunityCommentResponseDto> communityCommentResponseDtos,
                                                   boolean checkLike, boolean checkWish, boolean isWriter) {
        return new CommunityDetailResponseDto(
                community.getTitle(),
                community.getContent(),
                community.getViews(),
                community.getLikeCnt(),
                community.getCreateDate(),
                community.getModifiedDate(),
                community.getCategory().print(),
                community.getMember().getNickname(),
                checkLike, checkWish, isWriter,
                communityCommentResponseDtos
        );
    }
}
