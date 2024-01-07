package com.getcode.controller.project;

import com.getcode.config.s3.S3Service;
import com.getcode.config.security.SecurityUtil;
import com.getcode.domain.project.ProjectImage;
import com.getcode.dto.project.req.CommentRequestDto;
import com.getcode.dto.project.req.CommentUpdateRequestDto;
import com.getcode.dto.project.req.ProjectRequestDto;
import com.getcode.dto.project.req.ProjectUpdateRequestDto;
import com.getcode.dto.project.res.ProjectDetailResponseDto;
import com.getcode.dto.s3.S3FileDto;
import com.getcode.service.project.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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


        List<S3FileDto> files = s3Service.uploadFiles(fileType, multipartFiles);
        //파일 url리스트로 변환
        List<String> fileUrls = files.stream()
                                        .map(S3FileDto::getUploadFileUrl)
                                        .collect(Collectors.toList());

        //ProjectImage 타입의 url로 변환
        List<ProjectImage> imageUrls = fileUrls.stream()
                        .map(url -> ProjectImage.builder().imageUrl(url).build())
                                .collect(Collectors.toList());

        projectRequestDto.setImageUrls(imageUrls);

        projectService.insertProject(projectRequestDto);


        return ResponseEntity.ok().body(HttpStatus.OK);

    }


    @Operation(summary = "github url 중복확인 api")
    @GetMapping("/add/checkUrl")
    public ResponseEntity<Boolean> checkUrl(@Parameter(description = "github Url") @RequestBody String githubUrl){
       Boolean result = projectService.checkGithubUrlDuplication(githubUrl);
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "프로젝트 삭제 api")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProject(@Parameter(description = "프로젝트 아이디") @PathVariable Long id){

        String memberId = SecurityUtil.getCurrentMemberId();

        int res = 0;

        res = projectService.deleteProject(id, Long.parseLong(memberId));

        if(res <= 0 ){
            throw new RuntimeException();
        }

            return ResponseEntity.ok().body("삭제가 완료되었습니다.");
    }

/*
    @Operation(summary = "프로젝트 수정 api")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProject(@Parameter(description = "프로젝트 아이디") @PathVariable Long id,
                                           @RequestBody ProjectUpdateRequestDto requestDto
    ){

        String memberId = SecurityUtil.getCurrentMemberId();

        projectService.updateProject(id, requestDto,memberId);
        return ResponseEntity.ok().body("수정완료");
    }
*/

    @Operation(summary = "프로젝트 좋아요 api")
    @PostMapping("/like/{id}")
    ResponseEntity<?> likeProject(@Parameter(description = "프로젝트 아이디") @PathVariable Long id){

        String memberId = SecurityUtil.getCurrentMemberId();

        int result = projectService.likeProject(id, memberId);

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
        String memberId = SecurityUtil.getCurrentMemberId();

        int result = projectService.wishProject(id, memberId);

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


    @Operation(summary = "프로젝트 댓글 등록 api")
    @PostMapping("/detail/{id}/comment/add")
    ResponseEntity<?> addComment(@Parameter(description = "프로젝트 아이디") @PathVariable Long id,
                                 @RequestBody CommentRequestDto requestDto)
    {


        String memberId = SecurityUtil.getCurrentMemberId();

        projectService.addComment(id, memberId, requestDto);

        return ResponseEntity.ok().body("댓글 등록이 완료되었습니다.");

    }


    @Operation(summary = "프로젝트 댓글 삭제 api")
    @DeleteMapping("/detail/{projectId}/comment/delete/{id}")
    ResponseEntity<?> deleteComment(@Parameter(description = "프로젝트 아이디") @PathVariable Long projectId,
                                    @Parameter(description = "댓글 아이디") @PathVariable Long id)
    {

        String memberId = SecurityUtil.getCurrentMemberId();

        int result = projectService.deleteComment(id, projectId, memberId);

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
        String memberId = SecurityUtil.getCurrentMemberId();

        int result = projectService.updateComment(id, projectId, memberId, requestDto);

        if (result == 1){
            return ResponseEntity.ok().body("댓글 수정 완료");
        } else return ResponseEntity.ok().body("댓글 수정 실패");


    }

















}
