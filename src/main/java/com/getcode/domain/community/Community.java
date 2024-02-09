package com.getcode.domain.community;

import com.getcode.domain.common.BaseTimeEntity;
import com.getcode.domain.common.CommunityCategory;
import com.getcode.domain.member.Member;
import com.getcode.dto.community.requset.CommunityRequestDto;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
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
public class Community extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int views;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int likeCnt;

    @Enumerated(EnumType.STRING)
    private CommunityCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityComment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityLike> likes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishCommunity> wishes = new ArrayList<>();

    public void increaseViews() {
        this.views +=1;
    }

    public void increaseCount() {
        this.likeCnt +=1;
    }

    public void decreaseCount() {
        this.likeCnt -=1;
    }

    public void editCommunity(CommunityRequestDto req) {
        this.title = req.getTitle();
        this.content = req.getContent();
    }

}
