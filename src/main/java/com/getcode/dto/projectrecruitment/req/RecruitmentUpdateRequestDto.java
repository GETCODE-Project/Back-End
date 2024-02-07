package com.getcode.dto.projectrecruitment.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RecruitmentUpdateRequestDto {

    private String content;
    private String title;
    private String siDo;
    private String guGun;
    private Boolean online;
    private Boolean recruitment;
    private List<String> techStackList;
    private String subject;


}
