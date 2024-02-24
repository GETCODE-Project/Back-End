package com.getcode.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommunityCategory {

    QNA("QnA"), FREE("자유게시판"), COUNSEL("고민상담");

    private final String category;

    public static CommunityCategory fromString(String reqValue) {
        for (CommunityCategory category : CommunityCategory.values()) {
            if (category.category.equalsIgnoreCase(reqValue)) {
                return category;
            }
        }
        throw new IllegalArgumentException();
    }

    public String print(){
        return category;
    }
}
