package com.getcode.domain.study;

public enum SCategory {

    INTERVIEW("면접준비"), CODINGTEST("코테"), SURTIFICATE("자격증"), CS("cs"), OTHER("기타");


    private String category;
    SCategory(String category) {
        this.category = category;
    }

    public String print(){
        return category;
    }


}
