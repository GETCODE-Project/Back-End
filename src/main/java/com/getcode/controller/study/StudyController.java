package com.getcode.controller.study;


import com.getcode.domain.member.Member;
import com.getcode.dto.member.SignUpDto;
import com.getcode.dto.member.SignUpResponseDto;
import com.getcode.dto.study.StudyRequestDto;
import com.getcode.dto.study.StudyResponseDto;
import com.getcode.service.study.StudyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
