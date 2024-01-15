package com.getcode.controller.projectrecruitment;

import com.getcode.dto.projectrecruitment.req.ProjectRecruitmentRequestDto;
import com.getcode.dto.projectrecruitment.req.RecruitmentCommentRequestDto;
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








}
