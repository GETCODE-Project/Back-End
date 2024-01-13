package com.getcode.controller.project;

import com.getcode.config.s3.S3Service;
import com.getcode.config.security.SecurityUtil;
import com.getcode.domain.project.Project;
import com.getcode.domain.project.ProjectImage;
import com.getcode.dto.project.req.CommentRequestDto;
import com.getcode.dto.project.req.CommentUpdateRequestDto;
import com.getcode.dto.project.req.ProjectRequestDto;
import com.getcode.dto.project.req.ProjectUpdateRequestDto;
import com.getcode.dto.project.res.ProjectDetailResponseDto;
import com.getcode.dto.project.res.ProjectInfoResponseDto;
import com.getcode.dto.s3.S3FileDto;
import com.getcode.service.project.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "프로젝트 관련 기능 api명세")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/project")
@RestController
public class ProjectController {

    private final ProjectService projectService;
    private final S3Service s3Service;

    @Operation(summary = "프로젝트 정보 등록 api")
    @PostMapping("/add")
    public ResponseEntity<?> addProject(@Parameter(description = "프로젝트 등록 값", required = true)
                                        @Valid @RequestPart ProjectRequestDto projectRequestDto,
                                        @Parameter(description = "프로젝트 이미지")
                                        @RequestPart(name = "fileType") String fileType,
                                        @RequestPart(name = "files") List<MultipartFile> multipartFiles
    ){


        //확장성을 고려하여 List형태로 파일 저장
        List<S3FileDto> files = s3Service.uploadFiles(fileType, multipartFiles);
        //파일 url리스트로 변환
        List<String> fileUrls = files.stream()
                .map(S3FileDto::getUploadFileUrl)
                .collect(Collectors.toList());

        projectRequestDto.setImageUrls(fileUrls);

        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        projectService.insertProject(projectRequestDto, memberEmail);

        return ResponseEntity.ok().body("등록이 완료되었습니다.");

    }

/*
    @Operation(summary = "프로젝트 이미지 업로드 api")
    @PostMapping("/add/image")
    public ResponseEntity<?> insertImage(@Parameter(description = "프로젝트 이미지")
                                             @RequestPart(name = "fileType") String fileType,
                                         @RequestPart(name = "files") List<MultipartFile> multipartFiles)
    {

        List<String> imageUrl = projectService.insertImageInS3(fileType, multipartFiles);

        return ResponseEntity.ok().body(imageUrl);
    }
*/

    @Operation(summary = "github url 중복확인 api")
    @GetMapping("/add/checkUrl")
    public ResponseEntity<Boolean> checkUrl(@Parameter(description = "github Url") @RequestBody String githubUrl){
        Boolean result = projectService.checkGithubUrlDuplication(githubUrl);
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "프로젝트 삭제 api")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProject(@Parameter(description = "프로젝트 아이디") @PathVariable Long id){

        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        int res = 0;

        res = projectService.deleteProject(id, memberEmail);

        if(res <= 0 ){
            ResponseEntity.ok().body("삭제실패.");
        }

        return ResponseEntity.ok().body("삭제가 완료되었습니다.");
    }



    @Operation(summary = "프로젝트 수정 api")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProject(@Parameter(description = "프로젝트 아이디") @PathVariable Long id,
                                           @RequestPart ProjectUpdateRequestDto requestDto,
                                           @RequestPart(name = "fileType", required = false) String fileType,
                                           @RequestPart(name = "files", required = false) List<MultipartFile> multipartFiles
    ){

        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        projectService.updateProject(id, requestDto,memberEmail, fileType, multipartFiles);
        return ResponseEntity.ok().body("수정완료");
    }


    @Operation(summary = "프로젝트 좋아요 api")
    @PostMapping("/like/{id}")
    ResponseEntity<?> likeProject(@Parameter(description = "프로젝트 아이디") @PathVariable Long id){

        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        int result = projectService.likeProject(id, memberEmail);

        if(result == 1) {
            return ResponseEntity.ok().body("프로젝트 좋아요 성공");
        } else if (result == 0) {
            return ResponseEntity.ok().body("프로젝트 좋아요 삭제 성공");
        }
        return ResponseEntity.ok().body("프로젝트 좋아요 등록 또는 취소 실패");
    }


    @Operation(summary = "프로젝트 즐겨찾기 api")
    @PostMapping("/wish/{id}")
    ResponseEntity<?> wishProject(@Parameter(description = "프로젝트 아이디") @PathVariable Long id){
        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        int result = projectService.wishProject(id, memberEmail);

        if(result == 1){
            return ResponseEntity.ok().body("프로젝트 즐겨찾기 성공");
        } else if (result == 0){
            return ResponseEntity.ok().body("프로젝트 즐겨찾기 삭제 성공");
        }

        return ResponseEntity.ok().body("프로젝트 즐겨찾기 등록 또는 취소 실패");

    }



    @Operation(summary = "특정 프로젝트 상세정보 조회 api")
    @GetMapping("/detail/{id}")
    ResponseEntity<?> getProject(@Parameter(description = "프로젝트 아이디") @PathVariable Long id){

        ProjectDetailResponseDto responseDto = projectService.getProject(id);

        return ResponseEntity.ok().body(responseDto);

    }


    @Operation(summary = "전체 프로젝트 조회 api")
    @GetMapping("/all")
    ResponseEntity<List<ProjectInfoResponseDto>> getProjectList(@Parameter(description = "정렬 기준")
                                     @Pattern(regexp = "createDate|pastOrder|likeCnt|", message = "sort 값은 latestOrder, pastOrder, likeCnt,  중 하나여야 합니다")
                                     @RequestParam(defaultValue = "latestOrder") String sort,
                                                                @Parameter(description = "페이지 수")
                                     @Min(value = 0, message = "page값은 0이상이어야 합니다")
                                     @RequestParam(defaultValue = "0") int page,
                                                                @Parameter(description = "한 페이지에 담기는 개수")
                                     @Positive(message = "size값은 1이상이어야 합니다")
                                     @RequestParam(defaultValue = "10") int size,
                                                                @Parameter(description = "검색어") @RequestParam(defaultValue = "") String keyword,
                                                                @Parameter(description = "검색 조건") @RequestParam(defaultValue = "") List<String> subject,
                                                                @Parameter(description = "기술스택") @RequestParam(defaultValue = "") List<String> techStack,
                                                                @Parameter(description = "년도") @RequestParam(defaultValue = "") String year
    )
    {



      List<ProjectInfoResponseDto> projectLists = projectService.getProjectList(size, page, sort, keyword, subject, techStack, year);

        return ResponseEntity.ok().body(projectLists);
    }





    @Operation(summary = "프로젝트 댓글 등록 api")
    @PostMapping("/detail/{id}/comment/add")
    ResponseEntity<?> addComment(@Parameter(description = "프로젝트 아이디") @PathVariable Long id,
                                 @RequestBody CommentRequestDto requestDto)
    {


        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        projectService.addComment(id, memberEmail, requestDto);

        return ResponseEntity.ok().body("댓글 등록이 완료되었습니다.");

    }


    @Operation(summary = "프로젝트 댓글 삭제 api")
    @DeleteMapping("/detail/{projectId}/comment/delete/{id}")
    ResponseEntity<?> deleteComment(@Parameter(description = "프로젝트 아이디") @PathVariable Long projectId,
                                    @Parameter(description = "댓글 아이디") @PathVariable Long id)
    {

        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        int result = projectService.deleteComment(id, projectId, memberEmail);

        if(result == 1){
            return ResponseEntity.ok().body("댓글 삭제 완료");
        } else {
            return ResponseEntity.ok().body("댓글 삭제 실패");
        }

    }


    @Operation(summary = "프로젝트 댓글 수정 api")
    @PutMapping("/detail/{projectId}/comment/update/{id}")
    ResponseEntity<?> updateComment(@Parameter(description = "프로젝트 아이디") @PathVariable Long projectId,
                                    @Parameter(description = "프로젝트 아이디") @PathVariable Long id,
                                    @Parameter(description = "수정 내용") @RequestBody CommentUpdateRequestDto requestDto)
    {
        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        int result = projectService.updateComment(id, projectId, memberEmail, requestDto);

        if (result == 1){
            return ResponseEntity.ok().body("댓글 수정 완료");
        } else return ResponseEntity.ok().body("댓글 수정 실패");


    }

















}