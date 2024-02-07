package com.getcode.dto.community.requset;

import com.getcode.domain.community.Community;
import com.getcode.domain.community.CommunityComment;
import com.getcode.domain.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityCommentRequestDto {
    private String content;
    public CommunityComment toEntity(Community community, Member member) {
        return CommunityComment.builder()
                .content(content)
                .member(member)
                .community(community)
                .build();
    }
}
