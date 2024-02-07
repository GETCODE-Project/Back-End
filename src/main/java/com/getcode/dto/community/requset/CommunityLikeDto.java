package com.getcode.dto.community.requset;

import com.getcode.domain.community.Community;
import com.getcode.domain.community.CommunityLike;
import com.getcode.domain.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityLikeDto {
    private Member member;
    private Community community;

    public static CommunityLike toEntity(Member member, Community community) {
        return CommunityLike.builder()
                .member(member)
                .community(community)
                .build();
    }
}
