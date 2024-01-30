package com.getcode.domain.community;

import com.getcode.domain.common.BaseTimeEntity;
import com.getcode.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class CommunityComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    //연관관계 메서드//
    public void foreignKey(Member member){
        this.member = member;
        member.getCommunityComments().add(this);
    }

    public void foreignKey(Community community){
        this.community = community;
        community.getComments().add(this);
    }

    public void editComment(String content) {
        this.content = content;
    }

}
