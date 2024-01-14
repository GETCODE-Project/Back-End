package com.getcode.dto.community;

import com.getcode.domain.community.Community;
import com.getcode.dto.study.StudyEditDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityEditDto {
    private String title;
    private String content;

    public static CommunityEditDto toDto(Community community) {
        return new CommunityEditDto(
                community.getTitle(),
                community.getContent()
        );
    }
}
