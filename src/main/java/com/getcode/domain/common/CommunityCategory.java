package com.getcode.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommunityCategory {

    QNA("질문"), FREE("자유게시판"), COUNSEL("고민상담");

    private final String category;

    public String print(){
        return category;
    }
}
