package com.getcode.dto.project.res;

import java.util.List;

public class ProjectInfoResponseDto {

    private Long projectId;
    private String title;
    private String introduction;
    private int views;
    private int likeCnt;
    private List<ProjectStackResponseDto> techStackList;
    private List<ProjectSubjectResponseDto> projectSubjects;
    private ProjectImageUrlResponseDto imageUrl;
    private String memberNickName;

}
