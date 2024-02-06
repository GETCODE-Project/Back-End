package com.getcode.dto.project.res;

import com.getcode.domain.project.Project;
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

    private LocalDateTime createDate, modifiedDate;

    private List<ProjectStackResponseDto> techStacks;

    private String subject;
    //private List<ProjectSubjectResponseDto> subjects;

    //private ProjectImageUrlResponseDto imageUrl;

    private String memberNickName;

    private Boolean checkLike;
    private Boolean checkWish;



    public ProjectInfoResponseDto(Project project) {
        this.projectId = project.getId();
        this.title = project.getTitle();
        this.introduction = project.getIntroduction();
        this.views = project.getViews();
        this.likeCnt = project.getLikeCnt();
        this.modifiedDate = project.getModifiedDate();
        this.createDate = project.getCreateDate();
        /*
        List<ProjectImageUrlResponseDto> imageUrls = project.getProjectImages().stream()
                .limit(1) // 첫 번째 이미지만 선택
                .map(ProjectImageUrlResponseDto::new)
                .collect(Collectors.toList());

        if (!imageUrls.isEmpty() && imageUrls != null) {
            this.imageUrl = imageUrls.get(0);
        }
        */
        this.techStacks = project.getTechStacks().stream().map(ProjectStackResponseDto::new).collect(Collectors.toList());
        this.subject = project.getSubject().print();
        this.memberNickName = project.getMember().getNickname();

    }

    public void setCheckLike(Boolean projectLikedByUser){
        if(projectLikedByUser) {
            this.checkLike = true;
        } else {
            this.checkLike = false;
        }
    }


    public void setCheckWish(Boolean projectWishedByUser){
        if(projectWishedByUser) {
            this.checkWish = true;
        } else {
            this.checkWish = false;
        }

    }



}
