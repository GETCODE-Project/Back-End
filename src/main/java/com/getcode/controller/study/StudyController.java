package com.getcode.controller.study;



import com.getcode.dto.study.CreatedStudyResponseDto;
import com.getcode.dto.study.StudyCommentRequestDto;
import com.getcode.dto.study.StudyCommentResponseDto;
import com.getcode.dto.study.StudyEditDto;
import com.getcode.dto.study.StudyInfoResponseDto;
import com.getcode.dto.study.StudyRequestDto;
import com.getcode.service.study.StudyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "스터디 관련 API 명세")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;

    @Operation(summary = "스터디 모집글 작성",
            description = "제목: 2자 이상 / 내용: 2자 이상 / 지역: 필수 / 온라인 여부: 필수 / 주제: 리스트 형식"
                    + "연락 방법: 작성, ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED")
    })
    @PostMapping("/study")
    public ResponseEntity<CreatedStudyResponseDto> createStudy(@Valid @RequestBody StudyRequestDto req) {
        CreatedStudyResponseDto study = studyService.createStudy(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(study);
    }

    @Operation(summary = "스터디 모집글 조회", description = "PathVariable을 입력받아 게시글 조회후 조회수 1 증가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/study/{id}")
    public ResponseEntity<StudyInfoResponseDto> findStudy(@PathVariable(name = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(studyService.findStudy(id));
    }

    @Operation(summary = "모든 스터디 모집글 전체 조회", description = "최신순으로 모든 스터디 모집글 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/all-studies")
    public ResponseEntity<List<StudyInfoResponseDto>> findAllStudy() {
        return ResponseEntity.status(HttpStatus.OK).body(studyService.findAllStudy());
    }


    @Operation(summary = "로그인한 사용자가 작성한 스터디 모집글 전체 조회", description = "특정 사용자가 작성한 게시물 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/studies")
    public ResponseEntity<List<StudyInfoResponseDto>> findAllStudyByMember() {
        return ResponseEntity.status(HttpStatus.OK).body(studyService.findAllStudyByMember());
    }

    @Operation(summary = "스터디 게시글에 댓글", description = "스터디 Id를 입력받아 해당 스터디를 찾은 후 댓글")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/study/comment/{id}")
    public ResponseEntity<StudyCommentResponseDto> addComment(@PathVariable(name = "id") Long id,
                                                              @RequestBody StudyCommentRequestDto req) {
        return ResponseEntity.status(HttpStatus.OK).body(studyService.addComment(req, id));
    }

    @Operation(summary = "스터디 게시글 댓글 수정", description = "댓긋 Id를 입력받아 댓글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PutMapping("/study/comment/{id}")
    public ResponseEntity<StudyCommentResponseDto> editComment(@PathVariable(name = "id") Long id,
                                                              @RequestBody StudyCommentRequestDto req) {
        return ResponseEntity.status(HttpStatus.OK).body(studyService.editComment(req, id));
    }

    @Operation(summary = "스터디 게시글 댓글 삭제", description = "댓긋 Id를 입력받아 댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @DeleteMapping("/study/comment/{id}")
    public void deleteComment(@PathVariable(name = "id") Long id) {
        studyService.deleteComment(id);
    }



    @Operation(summary = "스터디 좋아요", description = "좋아요를 하면 전체 스터디 정보 리턴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/study-like/{id}")
    public ResponseEntity<StudyInfoResponseDto> addComment(@PathVariable(name = "id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(studyService.likeStudy(id));
    }



    @Operation(summary = "스터디 모집글 수정", description = "PathVariable, 스터디 변경내용을 입력받아 스터디 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PutMapping("/study/{id}")
    public ResponseEntity<StudyInfoResponseDto> editStudy(@PathVariable(name = "id") Long id,
                                                      @RequestBody StudyEditDto req) {
        return ResponseEntity.status(HttpStatus.OK).body(studyService.editStudy(id, req));
    }

    @Operation(summary = "스터디 모집글 삭제", description = "PathVariable 입력 후, 작성한 스터디 모집글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/study/{id}")
    public void deleteStudy(@PathVariable(name = "id") Long id) {
        studyService.deleteStudy(id);
    }

    @Operation(summary = "스터디 찜", description = "PathVariable 입력 후, 스터디 찜하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/study-wish/{id}")
    public void wishStudy(@PathVariable(name = "id") Long id) {
        studyService.wishStudy(id);
    }

}
