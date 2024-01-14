package com.getcode.domain.community;

import com.getcode.domain.common.BaseTimeEntity;
import com.getcode.domain.member.Member;
import com.getcode.dto.community.CommunityEditDto;
import com.getcode.global.CommunityTypeConverter;
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

    @Column(nullable = false)
    private int views;

    @Column(nullable = false)
    private int count;

    @Convert(converter = CommunityTypeConverter.class)
    private CommunityCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private List<CommunityComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private List<CommunityLike> likes = new ArrayList<>();

    public void increaseViews() {
        this.views +=1;
    }

    public void increaseCount() {
        this.count +=1;
    }

    public void decreaseCount() {
        this.count -=1;
    }

    public void editCommunity(CommunityEditDto req) {
        this.title = req.getTitle();
        this.content = req.getContent();
    }


}
