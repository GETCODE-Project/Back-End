package com.getcode.controller.study;


import com.getcode.domain.study.Study;
import com.getcode.dto.study.StudyEditDto;
import com.getcode.dto.study.StudyInfoResponseDto;
import com.getcode.dto.study.StudyRequestDto;
import com.getcode.dto.study.StudyResponseDto;
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
            description = "제목: 2자 이상 / 내용: 2자 이상 / 지역: 필수 / 온라인 여부: 필수" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED")
    })
    @PostMapping("/study")
    public ResponseEntity<StudyResponseDto> signup(@Valid @RequestBody StudyRequestDto req) {
        StudyResponseDto study = studyService.createStudy(req);
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
    public ResponseEntity<List<StudyResponseDto>> findAllStudy() {
        return ResponseEntity.status(HttpStatus.OK).body(studyService.findAllStudy());
    }


    @Operation(summary = "특정 사용자가 작성한 스터디 모집글 전체 조회", description = "스터디 목록 제목만 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/studies")
    public ResponseEntity<List<StudyResponseDto>> findAllStudyByMember() {
        return ResponseEntity.status(HttpStatus.OK).body(studyService.findAllStudyByMember());
    }

    @Operation(summary = "스터디 모집글 수정", description = "PathVariable, 스터디 변경내용을 입력받아 스터디 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PutMapping("/study/{id}")
    public ResponseEntity<StudyResponseDto> editStudy(@PathVariable(name = "id") Long id,
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

}
