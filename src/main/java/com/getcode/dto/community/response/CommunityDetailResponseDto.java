package com.getcode.dto.community.response;

import com.getcode.domain.community.Community;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.getcode.dto.member.MemberInfoDto;
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
    private int likeCnt;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private String category;
    private MemberInfoDto member;
    private boolean checkLike;
    private boolean checkWish;
    private boolean isWriter;




    public  CommunityDetailResponseDto(Community community,
                                                   Boolean checkLike, Boolean checkWish, Boolean isWriter) {

               this.title = community.getTitle();
               this.content = community.getContent();
               this.views = community.getViews();
               this.likeCnt = community.getLikeCnt();
               this.createDate = community.getCreateDate();
               this.modifiedDate = community.getModifiedDate();
               this.category = community.getCategory().print();
                this.member =MemberInfoDto.toDto(community.getMember());
                this.checkLike = checkLike;
                this.checkWish = checkWish;
                this.isWriter = isWriter;


    }

}
