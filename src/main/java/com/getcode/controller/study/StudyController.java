package com.getcode.controller.study;


import com.getcode.domain.member.Member;
import com.getcode.dto.member.SignUpDto;
import com.getcode.dto.member.SignUpResponseDto;
import com.getcode.dto.study.StudyRequestDto;
import com.getcode.dto.study.StudyResponseDto;
import com.getcode.service.study.StudyService;
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
    @PostMapping("/study")
    public ResponseEntity<StudyResponseDto> signup(@Valid @RequestBody StudyRequestDto req) {
        StudyResponseDto study = studyService.createStudy(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(study);
    }
}
