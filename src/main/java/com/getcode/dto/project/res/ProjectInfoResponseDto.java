package com.getcode.dto.project.res;

import com.getcode.domain.project.Project;
import com.getcode.dto.member.MemberInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private MemberInfoDto memberInfo;

    public ProjectInfoResponseDto(Project project) {
        this.projectId = project.getId();
        this.title = project.getTitle();
        this.views = project.getViews();
        this.likeCnt = project.getLikeCnt();

        List<ProjectImageUrlResponseDto> imageUrls = project.getProjectImages().stream()
                .limit(1) // 첫 번째 이미지만 선택
                .map(ProjectImageUrlResponseDto::new)
                .collect(Collectors.toList());

        if (!imageUrls.isEmpty()) {
            this.imageUrl = imageUrls.get(0);

            this.techStackList = project.getTechStacks().stream().map(ProjectStackResponseDto::new).collect(Collectors.toList());
            this.projectSubjects = project.getProjectSubjects().stream().map(ProjectSubjectResponseDto::new).collect(Collectors.toList());
            MemberInfoDto.toDto(project.getMember());
        }

    }
}
