package com.getcode.dto.community.response;

import com.getcode.domain.community.CommunityComment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityCommentResponseDto {
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private String email;
    private String nickname;
    private boolean isWriter;
    public static CommunityCommentResponseDto toDto(CommunityComment communityComment, boolean isWriter) {
        return new CommunityCommentResponseDto(
                communityComment.getContent(),
                communityComment.getCreateDate(),
                communityComment.getModifiedDate(),
                communityComment.getMember().getEmail(),
                communityComment.getMember().getNickname(),
                isWriter
        );
    }
}

