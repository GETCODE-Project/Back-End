package com.getcode.controller.projectrecruitment;

import com.getcode.dto.projectrecruitment.req.ProjectRecruitmentRequestDto;
import com.getcode.dto.projectrecruitment.req.RecruitmentCommentRequestDto;
import com.getcode.dto.projectrecruitment.req.RecruitmentCommentUpdateDto;
import com.getcode.dto.projectrecruitment.req.RecruitmentUpdateRequestDto;
import com.getcode.dto.projectrecruitment.res.ProjectRecruitmentDetailResDto;
import com.getcode.service.projectrecruitment.ProjectRecruitmentService;
import com.getcode.util.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "프로젝트 모집 api기능 명세")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/projectrecruitment")
@RestController
public class ProjectRecruitmentController {

    private final ProjectRecruitmentService projectRecruitmentService;
    @Operation(summary = "프로젝트 모집 글 등록 api")
    @PostMapping("/add")
    public ResponseEntity<?> insertProjectRecruitment(@Parameter(description = "프로젝트 모집 요청 값")
                                                          @Valid
                                                          @RequestBody ProjectRecruitmentRequestDto requestDto){

        projectRecruitmentService.insertProjectRecruitment(requestDto);
        return ResponseEntity.ok().body("글 등록 완료");

    }

    @Operation(summary = "프로젝트 모집 글 삭제 api")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProjectRecruitment(@Parameter(description = "프로젝트 모집 아이디")
                                                      @PathVariable Long id){
        int result = 0;
        result = projectRecruitmentService.deleteProjectRecruitment(id);

        if(result == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("글 삭제 성공");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("글 삭제 실패");
        }
    }


    @Operation(summary = "프로젝트 모집 댓글 등록 api")
    @PostMapping("/detail/{id}/comment/add")
    public ResponseEntity<?> addComment(@Parameter(description = "프로젝트 모집 아이디")
                                        @PathVariable Long id,
                                        @RequestBody RecruitmentCommentRequestDto requestDto)
    {
        projectRecruitmentService.addComment(id, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body("댓글 등록 완료");
    }

    @Operation(summary = "프로젝트 모집 댓글 수정 api")
    @PutMapping("/detail/{projectId}/comment/update/{commentId}")
    public ResponseEntity<?> updateComment(@Parameter(description = "프로젝트 모집 아이디")
                                           @PathVariable Long projectId,
                                           @Parameter(description = "프로젝트 모집 댓글 아이디")
                                           @PathVariable Long commentId,
                                           @RequestBody RecruitmentCommentUpdateDto requestDto
                                           ){

        projectRecruitmentService.updateComment(projectId, commentId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 수정 완료");

    }

    @Operation(summary = "프로젝트 모집 댓글 수정 api")
    @DeleteMapping("/detail/{projectId}/comment/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@Parameter(description = "프로젝트 모집 아이디")
                                               @PathVariable Long projectId,
                                           @Parameter(description = "프로젝트 모집 댓글 아이디")
                                               @PathVariable Long commentId)
    {
        projectRecruitmentService.deleteComment(projectId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제 완료");
    }


    @Operation(summary = "프로젝트 모집 좋아요 api")
    @PostMapping("/like/{projectId}")
    public ResponseEntity<?> likeProjectRecruitment(@Parameter(description = "프로젝트 모집 아이디")
                                                        @PathVariable Long projectId)
    {

        int result = projectRecruitmentService.likeProjectRecruitment(projectId);
        if(result == 1){
            return ResponseEntity.status(HttpStatus.OK).body("좋아요 등록");
        } else if (result == -1) {
            return ResponseEntity.status(HttpStatus.OK).body("좋아요 삭제");
        } else {
          return ResponseEntity.status(HttpStatus.OK).body("좋아요 등록 또는 삭제 실패");
        }

    }

    @Operation(summary = "프로젝트 모집 찜 api")
    @PostMapping("/wish/{projectId}")
    public ResponseEntity<?> wishProjectRecruitment(@Parameter(description = "프로젝트 모집 아이디")
                                                        @PathVariable Long projectId)
    {

        int result = projectRecruitmentService.wishProjectRecruitment(projectId);
        if(result == 1){
            return ResponseEntity.status(HttpStatus.OK).body("찜 등록");
        } else if (result == -1) {
            return ResponseEntity.status(HttpStatus.OK).body("찜 삭제");
        } else {
          return ResponseEntity.status(HttpStatus.OK).body("찜 등록 또는 삭제 실패");
        }

    }

    @Operation(summary = "프로젝트 모집 상세조회 api")
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetailRecruitment(@Parameter(description = "프로젝트 모집 아이디")
                                                  @PathVariable Long id)
    {

        ProjectRecruitmentDetailResDto resDto = projectRecruitmentService.getDetailRecruitment(id);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }





}
