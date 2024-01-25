package com.getcode.dto.project.res;

import com.getcode.config.security.SecurityUtil;
import com.getcode.domain.project.Project;
import com.getcode.domain.project.ProjectLike;
import com.getcode.domain.project.WishProject;
import com.getcode.dto.member.MemberInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ProjectInfoResponseDto {

    private Long projectId;

    private String title;

    private String introduction;

    private int views;

    private int likeCnt;

    private LocalDateTime dateTime;

    private List<ProjectStackResponseDto> techStackList;

    private List<ProjectSubjectResponseDto> projectSubjects;

    private ProjectImageUrlResponseDto imageUrl;

    private String memberNickName;

    private List<Boolean> checkLike;
    private List<Boolean> checkWish;



    public ProjectInfoResponseDto(Project project) {
        this.projectId = project.getId();
        this.title = project.getTitle();
        this.introduction = project.getIntroduction();
        this.views = project.getViews();
        this.likeCnt = project.getLikeCnt();
        this.dateTime = project.getModifiedDate();
        List<ProjectImageUrlResponseDto> imageUrls = project.getProjectImages().stream()
                .limit(1) // 첫 번째 이미지만 선택
                .map(ProjectImageUrlResponseDto::new)
                .collect(Collectors.toList());

        if (!imageUrls.isEmpty() && imageUrls != null) {
            this.imageUrl = imageUrls.get(0);
        }
        this.techStackList = project.getTechStacks().stream().map(ProjectStackResponseDto::new).collect(Collectors.toList());
        this.projectSubjects = project.getProjectSubjects().stream().map(ProjectSubjectResponseDto::new).collect(Collectors.toList());
        this.memberNickName = project.getMember().getNickname();

    }




}
