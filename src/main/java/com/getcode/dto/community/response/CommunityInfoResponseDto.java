package com.getcode.dto.community.response;

import com.getcode.domain.community.Community;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CommunityInfoResponseDto {
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

    public static CommunityInfoResponseDto toDto(Community community, boolean checkLike, boolean checkWish) {
        return new CommunityInfoResponseDto(
                community.getTitle(),
                community.getContent().substring(0,15),
                community.getViews(),
                community.getLikeCnt(),
                community.getCreateDate(),
                community.getModifiedDate(),
                community.getCategory().print(),
                community.getMember().getNickname(),
                checkLike,
                checkWish
        );
    }
}
