package com.getcode.domain.community;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommunityCategory {

    QNA("질문"), FREE("자유게시판"), COUNSEL("고민상담");

    private final String category;
}
