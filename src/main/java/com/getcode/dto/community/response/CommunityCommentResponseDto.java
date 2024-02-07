package com.getcode.dto.community.response;

import com.getcode.domain.community.CommunityComment;
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
    private String modifiedDate;
    private String email;
    private String nickname;
    public static CommunityCommentResponseDto toDto(CommunityComment communityComment) {
        return new CommunityCommentResponseDto(
                communityComment.getContent(),
                communityComment.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
                communityComment.getMember().getEmail(),
                communityComment.getMember().getNickname()
        );
    }
}

