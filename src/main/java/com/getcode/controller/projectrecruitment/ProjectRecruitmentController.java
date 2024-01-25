package com.getcode.controller.projectrecruitment;

import com.getcode.dto.projectrecruitment.req.ProjectRecruitmentRequestDto;
import com.getcode.dto.projectrecruitment.req.RecruitmentCommentRequestDto;
import com.getcode.dto.projectrecruitment.req.RecruitmentCommentUpdateDto;
import com.getcode.dto.projectrecruitment.req.RecruitmentUpdateRequestDto;
import com.getcode.dto.projectrecruitment.res.ProjectRecruitmentDetailResDto;
import com.getcode.dto.projectrecruitment.res.ProjectRecruitmentInfoResDto;
import com.getcode.service.projectrecruitment.ProjectRecruitmentService;
import com.getcode.util.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @DeleteMapping("/{id}/delete")
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
    @PutMapping("/detail/{recruitmentId}/comment/update/{id}")
    public ResponseEntity<?> updateComment(@Parameter(description = "프로젝트 모집 아이디")
                                           @PathVariable Long recruitmentId,
                                           @Parameter(description = "프로젝트 모집 댓글 아이디")
                                           @PathVariable Long id,
                                           @RequestBody RecruitmentCommentUpdateDto requestDto
                                           ){

        projectRecruitmentService.updateComment(recruitmentId, id, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 수정 완료");

    }

    @Operation(summary = "프로젝트 모집 댓글 삭제 api")
    @DeleteMapping("/detail/{recruitmentId}/comment/delete/{id}")
    public ResponseEntity<?> deleteComment(@Parameter(description = "프로젝트 모집 아이디")
                                               @PathVariable Long recruitmentId,
                                           @Parameter(description = "프로젝트 모집 댓글 아이디")
                                               @PathVariable Long id)
    {
        projectRecruitmentService.deleteComment(recruitmentId, id);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제 완료");
    }


    @Operation(summary = "프로젝트 모집 좋아요 api")
    @PostMapping("/{recruitmentId}/like")
    public ResponseEntity<?> likeProjectRecruitment(@Parameter(description = "프로젝트 모집 아이디")
                                                        @PathVariable Long recruitmentId)
    {

        int result = projectRecruitmentService.likeProjectRecruitment(recruitmentId);
        if(result == 1){
            return ResponseEntity.status(HttpStatus.OK).body("좋아요 등록");
        } else if (result == -1) {
            return ResponseEntity.status(HttpStatus.OK).body("좋아요 삭제");
        } else {
          return ResponseEntity.status(HttpStatus.OK).body("좋아요 등록 또는 삭제 실패");
        }

    }

    @Operation(summary = "프로젝트 모집 찜 api")
    @PostMapping("/{recruitmentId}/wish")
    public ResponseEntity<?> wishProjectRecruitment(@Parameter(description = "프로젝트 모집 아이디")
                                                        @PathVariable Long recruitmentId)
    {

        int result = projectRecruitmentService.wishProjectRecruitment(recruitmentId);
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

    @Operation(summary = "프로젝트 모집글 전체조회 api")
    @GetMapping("/all")
    public ResponseEntity<?> getAllRecruitment(@Parameter(description = "정렬 기준: latestOrder, pastOrder, likeCnt중 하나여야 합니다.")
                                                   @RequestParam(defaultValue = "latestOrder", required = false) String sort,
                                               @Parameter(description = "페이지 수")
                                                   @Min(value = 0, message = "page값은 0이상이어야 합니다")
                                                   @RequestParam(defaultValue = "0") int page,
                                               @Parameter(description = "한 페이지에 담기는 개수")
                                                   @Positive(message = "size값은 1이상이어야 합니다")
                                                   @RequestParam(defaultValue = "10") int size,
                                               @Parameter(description = "검색어") @RequestParam(defaultValue = "", required = false) String keyword,
                                               @Parameter(description = "검색 조건") @RequestParam(defaultValue = "", required = false) List<String> subject,
                                               @Parameter(description = "기술스택") @RequestParam(defaultValue = "", required = false) List<String> techStack,
                                               @Parameter(description = "년도") @RequestParam(defaultValue = "2024", required = false) Integer year,
                                               @Parameter(description = "사용자 id")@RequestParam(required = false) Long memberId)
    {

        List<ProjectRecruitmentInfoResDto> resDtoList = projectRecruitmentService.getAllRecuritment(sort, page, size, keyword, subject, techStack, year, memberId);

        return ResponseEntity.status(HttpStatus.OK).body(resDtoList);
    }

    @Operation(summary = "프로젝트 모집글 수정 api")
    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateRecruitment(@RequestBody RecruitmentUpdateRequestDto requestDto,
                                               @Parameter(description = "프로젝트 모집글 id") @PathVariable Long id)
    {

        projectRecruitmentService.updateRecruitment(requestDto, id);

        return ResponseEntity.status(HttpStatus.OK).body("수정 완료.");
    }



}
