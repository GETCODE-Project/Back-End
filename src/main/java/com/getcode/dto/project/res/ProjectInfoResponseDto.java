package com.getcode.dto.project.res;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectInfoResponseDto {
    @Schema(description = "프로젝트 아이디")
    private Long projectId;
    @Schema(description = "프로젝트 제목")
    private String title;
    @Schema(description = "프로젝트 한 줄 소개")
    private String introduction;
    @Schema(description = "조회수")
    private int views;
    @Schema(description = "좋아요수")
    private int likeCnt;
    @Schema(description = "날짜 및 시간 ")
    private LocalDateTime dateTime;
    @Schema(description = "프로젝트 기술 스택", example = "[Springboot, Java]")
    private List<ProjectStackResponseDto> techStackList;
    @Schema(description = "프로젝트 주제", example = "[여행, 공유서비스]")
    private List<ProjectSubjectResponseDto> projectSubjects;
    @Schema(description = "프로젝트 썸네일 이미지")
    private ProjectImageUrlResponseDto imageUrl;
    @Schema(description = "작성자")
    private String memberNickName;

}
