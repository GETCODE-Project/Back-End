package com.getcode.domain.community;

public enum CCategory {

    QNA("QnA"), FREEBOARD("자유게시판"), COUNSEL("고민상담")
    ;


    private String category;
    CCategory(String category) {

        this.category = category;
    }

    public String print(){
        return category;
    }

}
