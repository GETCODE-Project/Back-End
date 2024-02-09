package com.getcode.domain.community;

import com.getcode.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class WishCommunity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_community_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    /*연관 관계 메서드*/ // 메서드 오버로딩
    private void foreignkey(Member member){
        this.member = member;
        member.getCommunityWishes().add(this);
    }

    private void foreignkey(Community community){
        this.community = community;
        community.getWishes().add(this);
    }

    public static WishCommunity createWishCommunity(Member newMember,
                                                    Community newCommunity) {
        WishCommunity wishCommunity = new WishCommunity();

        wishCommunity.foreignkey(newMember);
        wishCommunity.foreignkey(newCommunity);

        return wishCommunity;
    }
}
