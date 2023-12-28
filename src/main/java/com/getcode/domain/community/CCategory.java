package com.getcode.domain.community;

public enum CCategory {

    INTERVIEW("면접준비"), CODINGTEST("코테"), SURTIFICATE("자격증"), CS("cs"), OTHER("기타")
    ;


    private String category;
    CCategory(String category) {

        this.category = category;
    }

    public String print(){
        return category;
    }

}
